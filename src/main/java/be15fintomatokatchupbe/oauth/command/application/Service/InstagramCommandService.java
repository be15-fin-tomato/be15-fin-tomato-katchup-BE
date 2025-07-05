package be15fintomatokatchupbe.oauth.command.application.Service;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Instagram;
import be15fintomatokatchupbe.influencer.command.domain.repository.InfluencerRepository;
import be15fintomatokatchupbe.influencer.command.domain.repository.InstagramRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.infra.redis.InstagramTokenRepository;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import be15fintomatokatchupbe.oauth.query.service.InstagramTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramCommandService {

    private final InstagramRepository instagramRepository;
    private final InstagramTokenRepository instagramTokenRepository;
    private final InfluencerRepository influencerRepository;

    @Transactional
    public void disconnectYoutubeAccount(Long influencerId) {
        // 1. 연동된 Youtube 정보 조회
        Instagram instagram = instagramRepository.findByInfluencerId(influencerId)
                .orElseThrow(() -> new BusinessException(OAuthErrorCode.INSTAGRAM_ACCOUNT_NOT_FOUND));

        String channelId = instagram.getAccountId();

        // 2. Redis 토큰 삭제
        instagramTokenRepository.delete(channelId);

        // 3. Youtube 엔티티 삭제 (influencerId 기준)
        instagramRepository.deleteByInfluencerId(influencerId);

        // 4. Influencer 연동 상태 해제 (isConnected = 'N')
        disconnectInfluencerInstagram(influencerId);

        log.info("🧹 인스타그램 연동 해제 완료 - influencerId={}, channelId={}", influencerId, channelId);
    }

    @Transactional
    public void disconnectInfluencerInstagram(Long influencerId) {
        Influencer influencer = influencerRepository.findById(influencerId)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));
        influencer.setYoutubeIsConnected(StatusType.N);
    }

}
