package be15fintomatokatchupbe.oauth.command.application.Service;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.application.support.YoutubeHelperService;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.influencer.command.domain.repository.InfluencerRepository;
import be15fintomatokatchupbe.influencer.command.domain.repository.YoutubeRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.infra.redis.YoutubeTokenRepository;
import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeStatsSnapshot;
import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeVideoSnapshot;
import be15fintomatokatchupbe.oauth.command.application.repository.YoutubeStatsSnapshotRepository;
import be15fintomatokatchupbe.oauth.command.application.repository.YoutubeVideoSnapshotRepository;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeChannelInfoResponse;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeAnalyticsQueryService;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeCommandService {

    private final YoutubeHelperService youtubeHelperService;
    private final YoutubeTokenRepository youtubeTokenRepository;
    private final YoutubeRepository youtubeRepository;
    private final YoutubeStatsSnapshotRepository statsSnapshotRepository;
    private final YoutubeVideoSnapshotRepository videoSnapshotRepository;
    private final InfluencerRepository influencerRepository;
    private final YoutubeAnalyticsQueryService youtubeAnalyticsQueryService;
    private final YoutubeOAuthQueryService youtubeOAuthQueryService;
    private final YoutubeStatsSnapshotRepository youtubeStatsSnapshotRepository;
    private final YoutubeVideoSnapshotRepository youtubeVideoSnapshotRepository;

    // 최초 연동: 채널 정보 조회 후 DB 저장
    public void registerYoutubeAccount(Long influencerId, String accessToken, String refreshToken) {
        // 1. 채널 정보 조회
        String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&mine=true";
        YoutubeChannelInfoResponse youtubeChannelInfo =
                youtubeOAuthQueryService.getWithAuth(url, accessToken, YoutubeChannelInfoResponse.class);

        if (youtubeChannelInfo.items() == null || youtubeChannelInfo.items().isEmpty()) {
            throw new BusinessException(OAuthErrorCode.YOUTUBE_CHANNEL_NOT_FOUND);
        }

        YoutubeChannelInfoResponse.Item item = youtubeChannelInfo.items().get(0);

        // 2. 정보 추출
        String channelId = item.id();
        String title = item.snippet().title();
        String thumbnail = item.snippet().thumbnails().defaultThumbnail().url();
        Long subscriberCount = item.statistics().subscriberCount();

        // 3. 유튜브 엔티티 저장
        Youtube youtube = Youtube.builder()
                .influencerId(influencerId)
                .channelId(channelId)
                .title(title)
                .thumbnail(thumbnail)
                .refreshToken(refreshToken)
                .subscriber(subscriberCount)
                .build();

        youtubeHelperService.saveOrUpdate(youtube);

        log.info("✅ 유튜브 계정 연동 완료 - influencerId={}, channelId={}", influencerId, channelId);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        try {
            collectAndSaveYoutubeStatsSnapshot(influencerId, startDate.toString(), endDate.toString());
            log.info("✅ 신규 계정 통계 스냅샷 즉시 저장 완료 - influencerId={}", influencerId);
        } catch (BusinessException e) {
            log.warn("⚠️ 신규 계정 통계 스냅샷 즉시 저장 실패 (비즈니스 예외): {}", e.getMessage());
        } catch (Exception e) {
            log.error("❌ 신규 계정 통계 스냅샷 즉시 저장 실패 (시스템 예외): {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void registerYoutubeByOAuth(String code, Long influencerId) {
        YoutubeOAuthQueryService.GoogleTokenResponse tokenResponse = youtubeOAuthQueryService.getToken(code);

        youtubeOAuthQueryService.saveRefreshTokenByAccess(tokenResponse);

        Influencer influencer = influencerRepository.findById(influencerId)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

        influencer.updateYoutubeStatus(StatusType.Y);

        registerYoutubeAccount(influencerId, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

    @Transactional
    public void disconnectYoutubeAccount(Long influencerId) {
        // 1. 연동된 Youtube 정보 조회
        Youtube youtube = youtubeHelperService.findByInfluencerId(influencerId);
        if (youtube == null) {
            throw new BusinessException(OAuthErrorCode.YOUTUBE_CHANNEL_NOT_FOUND);
        }

        String channelId = youtube.getChannelId();

        // 2. Redis 토큰 삭제
        youtubeTokenRepository.delete(channelId);
        youtubeTokenRepository.deleteAccessToken(channelId);

        // 3. Youtube 관련 데이터 삭제 (순서 변경: 자식 먼저 삭제)
        youtubeVideoSnapshotRepository.deleteByInfluencerId(influencerId);
        youtubeStatsSnapshotRepository.deleteByInfluencerId(influencerId);

        // 4. Youtube 엔티티 삭제 (influencerId 기준)
        youtubeHelperService.deleteYoutubeByInfluencerId(influencerId);

        // 5. Influencer 연동 상태 해제 (isConnected = 'N')
        youtubeHelperService.disconnectInfluencerYoutube(influencerId);

        log.info("🧹 유튜브 연동 해제 완료 - influencerId={}, channelId={}", influencerId, channelId);
    }

    @Transactional
    public void saveOrUpdateSnapshot(Long influencerId, YoutubeStatsResponse response) {
        Influencer influencer = influencerRepository.findById(influencerId)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

        Optional<YoutubeStatsSnapshot> optional = statsSnapshotRepository.findByInfluencerId(influencerId);

        YoutubeStatsSnapshot snapshot = optional
                .map(existing -> existing.updateFrom(response))
                .orElseGet(() -> YoutubeStatsSnapshot.builder()
                        .influencerId(influencerId)
                        .totalVideos(response.getTotalVideos())
                        .avgViews(response.getAvgViews())
                        .avgLikes(response.getAvgLikes())
                        .avgComments(response.getAvgComments())
                        .dailyAvgViews(response.getDailyAvgViews())
                        .monthlyAvgViews(response.getMonthlyAvgViews())
                        .age1824(response.getSubscriberAgeRatio().getOrDefault("age18-24", 0.0))
                        .age2534(response.getSubscriberAgeRatio().getOrDefault("age25-34", 0.0))
                        .age3544(response.getSubscriberAgeRatio().getOrDefault("age35-44", 0.0))
                        .age4554(response.getSubscriberAgeRatio().getOrDefault("age45-54", 0.0))
                        .genderFemale(response.getSubscriberGenderRatio().getOrDefault("female", 0.0))
                        .genderMale(response.getSubscriberGenderRatio().getOrDefault("male", 0.0))
                        .subscriberChangeDaily(response.getSubscriberChange().getOrDefault("daily", 0))
                        .subscriberChangeWeekly(response.getSubscriberChange().getOrDefault("weekly", 0))
                        .subscriberChangeMonthly(response.getSubscriberChange().getOrDefault("monthly", 0))
                        .subscribedRatio(response.getSubscribedVsNot().getOrDefault("subscribed", 0.0))
                        .notSubscribedRatio(response.getSubscribedVsNot().getOrDefault("notSubscribed", 0.0))
                        .createdAt(LocalDateTime.now())
                        .build());

        YoutubeStatsSnapshot saved = statsSnapshotRepository.save(snapshot);

        // 영상 스냅샷 먼저 삭제
        videoSnapshotRepository.deleteBySnapshotId(saved.getId());

        // 영상 스냅샷 재저장
        List<YoutubeVideoSnapshot> videoSnapshots = response.getTopVideos().stream()
                .map(video -> YoutubeVideoSnapshot.builder()
                        .title(video.getTitle())
                        .videoId(video.getVideoId())
                        .views(video.getViews())
                        .likes(video.getLikes())
                        .comments(video.getComments())
                        .thumbnailUrl(video.getThumbnailUrl())
                        .snapshot(saved)
                        .influencer(influencer)
                        .publishedAt(video.getPublishedAt())
                        .build())
                .toList();

        videoSnapshotRepository.saveAll(videoSnapshots);
    }

    @Transactional
    public void collectAndSaveYoutubeStatsSnapshot(Long influencerId, String startDate, String endDate) {
        String channelId;

        try {
            Youtube youtube = youtubeRepository.findById(influencerId)
                    .orElseThrow(() -> new BusinessException(OAuthErrorCode.YOUTUBE_CHANNEL_NOT_FOUND)); // 적절한 예외 처리
            channelId = youtube.getChannelId();


            log.info("📊 통계 스냅샷 수집 시작 - influencerId={}, channelId={}", influencerId, channelId);

            YoutubeStatsResponse response = youtubeAnalyticsQueryService.getYoutubeStatsByInfluencer(
                    influencerId, startDate, endDate
            );

            saveOrUpdateSnapshot(influencerId, response);

            log.info("✅ 통계 스냅샷 수집 및 저장 성공 - influencerId={}", influencerId);

        } catch (BusinessException e) {
            log.warn("⛔️ 비즈니스 예외 발생 (스냅샷 수집) - influencerId={}, message={}", influencerId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("🔥 시스템 예외 발생 (스냅샷 수집) - influencerId={}, message={}", influencerId, e.getMessage(), e);
            throw new BusinessException(OAuthErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}

