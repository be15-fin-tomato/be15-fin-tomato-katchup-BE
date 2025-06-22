package be15fintomatokatchupbe.campaign.command.application.support;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Campaign;
import be15fintomatokatchupbe.campaign.command.domain.repository.CampaignRepository;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CampaignHelperService {
    private final CampaignRepository campaignRepository;

    public Campaign findValidCampaign(Long campaignId){
        return campaignRepository.findByCampaignIdAndIsDeleted(campaignId, StatusType.N)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));
    }

}
