package be15fintomatokatchupbe.oauth.command.application.Service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.application.support.YoutubeHelperService;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.infra.redis.YoutubeTokenRepository;
import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeStatsSnapshot;
import be15fintomatokatchupbe.oauth.command.application.repository.YoutubeStatsSnapshotRepository;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeCommandService {

    private final YoutubeHelperService youtubeHelperService;
    private final YoutubeTokenRepository youtubeTokenRepository;
    private final YoutubeStatsSnapshotRepository snapshotRepository;

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
    public void saveSnapshot(Long influencerId, YoutubeStatsResponse dto) {
        YoutubeStatsSnapshot snapshot = YoutubeStatsSnapshot.builder()
                .influencerId(influencerId)
                .totalVideos(dto.getTotalVideos())
                .avgViews(dto.getAvgViews())
                .avgLikes(dto.getAvgLikes())
                .avgComments(dto.getAvgComments())
                .dailyAvgViews(dto.getDailyAvgViews())
                .monthlyAvgViews(dto.getMonthlyAvgViews())

                .age1824(dto.getSubscriberAgeRatio().getOrDefault("age18-24", 0.0))
                .age2534(dto.getSubscriberAgeRatio().getOrDefault("age25-34", 0.0))
                .age3544(dto.getSubscriberAgeRatio().getOrDefault("age35-44", 0.0))
                .age4554(dto.getSubscriberAgeRatio().getOrDefault("age45-54", 0.0))

                .genderFemale(dto.getSubscriberGenderRatio().getOrDefault("female", 0.0))
                .genderMale(dto.getSubscriberGenderRatio().getOrDefault("male", 0.0))

                .subscriberChangeDaily(dto.getSubscriberChange().getOrDefault("daily", 0))
                .subscriberChangeWeekly(dto.getSubscriberChange().getOrDefault("weekly", 0))
                .subscriberChangeMonthly(dto.getSubscriberChange().getOrDefault("monthly", 0))

                .subscribedRatio(dto.getSubscribedVsNot().getOrDefault("subscribed", 0.0))
                .notSubscribedRatio(dto.getSubscribedVsNot().getOrDefault("notSubscribed", 0.0))

                .createdAt(LocalDateTime.now())
                .build();

        snapshotRepository.save(snapshot);
    }

}
