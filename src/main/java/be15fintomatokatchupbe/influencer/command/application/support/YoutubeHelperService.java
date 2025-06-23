package be15fintomatokatchupbe.influencer.command.application.support;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.influencer.command.domain.repository.YoutubeRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YoutubeHelperService {
    private final YoutubeRepository youtubeRepository;

    public Youtube findYoutube(String channelId){
        return youtubeRepository.findByChannelId(channelId)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.YOUTUBE_ACCOUNT_NOT_FOUND));
    }
}
