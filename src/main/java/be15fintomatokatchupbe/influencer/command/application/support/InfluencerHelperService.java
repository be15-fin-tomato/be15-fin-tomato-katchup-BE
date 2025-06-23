package be15fintomatokatchupbe.influencer.command.application.support;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.command.domain.repository.InfluencerRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InfluencerHelperService {
    private final InfluencerRepository influencerRepository;

    public Influencer findValidInfluencer(Long influencerId){
        return influencerRepository.findByIdAndIsDeleted(influencerId, StatusType.N)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

    }
}
