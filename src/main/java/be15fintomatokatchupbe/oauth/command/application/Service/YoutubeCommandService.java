package be15fintomatokatchupbe.oauth.command.application.Service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.application.support.YoutubeHelperService;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.infra.redis.YoutubeTokenRepository;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeCommandService {

    private final YoutubeHelperService youtubeHelperService;
    private final YoutubeTokenRepository youtubeTokenRepository;

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

}
