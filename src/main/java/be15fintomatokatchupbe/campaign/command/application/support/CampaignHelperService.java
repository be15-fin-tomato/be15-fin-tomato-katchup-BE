package be15fintomatokatchupbe.campaign.command.application.support;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Campaign;
import be15fintomatokatchupbe.campaign.command.domain.repository.CampaignRepository;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CampaignHelperService {
    private final CampaignRepository campaignRepository;

    public Campaign findValidCampaign(Long campaignId){
        log.info("[Helper] findValidCampaign 호출됨. campaignId = {}", campaignId);
        return campaignRepository.findByCampaignIdAndIsDeleted(campaignId, StatusType.N)
                .orElseThrow(() -> {
                    log.warn("[Helper] 캠페인 조회 실패 - 존재하지 않거나 삭제된 캠페인. campaignId = {}", campaignId);
                    return new BusinessException(CampaignErrorCode.CAMPAIGN_NOT_FOUND);
                });
    }

}
