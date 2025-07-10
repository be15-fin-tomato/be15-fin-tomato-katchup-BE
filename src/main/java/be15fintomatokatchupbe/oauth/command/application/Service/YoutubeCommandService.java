package be15fintomatokatchupbe.oauth.command.application.Service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.application.support.YoutubeHelperService;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.influencer.command.domain.repository.InfluencerRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.infra.redis.YoutubeTokenRepository;
import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeStatsSnapshot;
import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeVideoSnapshot;
import be15fintomatokatchupbe.oauth.command.application.repository.YoutubeStatsSnapshotRepository;
import be15fintomatokatchupbe.oauth.command.application.repository.YoutubeVideoSnapshotRepository;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeCommandService {

    private final YoutubeHelperService youtubeHelperService;
    private final YoutubeTokenRepository youtubeTokenRepository;
    private final YoutubeStatsSnapshotRepository statsSnapshotRepository;
    private final YoutubeVideoSnapshotRepository videoSnapshotRepository;
    private final InfluencerRepository influencerRepository;

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

        // 3. Youtube 엔티티 삭제 (influencerId 기준)
        youtubeHelperService.deleteYoutubeByInfluencerId(influencerId);

        // 4. Influencer 연동 상태 해제 (isConnected = 'N')
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
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();

        videoSnapshotRepository.saveAll(videoSnapshots);
    }

}

