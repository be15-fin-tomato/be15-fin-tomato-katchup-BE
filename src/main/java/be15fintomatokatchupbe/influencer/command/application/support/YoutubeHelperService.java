package be15fintomatokatchupbe.influencer.command.application.support;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.influencer.command.domain.repository.InfluencerRepository;
import be15fintomatokatchupbe.influencer.command.domain.repository.YoutubeRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class YoutubeHelperService {
    private final YoutubeRepository youtubeRepository;
    private final InfluencerRepository influencerRepository;

    public Youtube findYoutube(String channelId){
        return youtubeRepository.findByChannelId(channelId)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.YOUTUBE_ACCOUNT_NOT_FOUND));
    }

    public void save(Youtube youtube) {
        youtubeRepository.save(youtube);
    }

    public Youtube findByInfluencerId(Long influencerId) {
        return youtubeRepository.findYoutubeByInfluencerId(influencerId)
                .orElseThrow(() -> new BusinessException(OAuthErrorCode.YOUTUBE_CHANNEL_NOT_FOUND));
    }

    @Transactional
    public void disconnectInfluencerYoutube(Long influencerId) {
        Influencer influencer = influencerRepository.findById(influencerId)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));
        influencer.setYoutubeIsConnected(StatusType.N);
    }

    @Transactional
    public void deleteYoutubeByInfluencerId(Long influencerId) {
        Youtube youtube = youtubeRepository.findYoutubeByInfluencerId(influencerId)
                .orElseThrow(() -> new BusinessException(OAuthErrorCode.YOUTUBE_CHANNEL_NOT_FOUND));
        youtubeRepository.delete(youtube);
    }

}
