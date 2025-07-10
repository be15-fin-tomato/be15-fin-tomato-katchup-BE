package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.mapper.CampaignWithCategoryDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CampaignAiResponse {
    List<CampaignWithCategoryDTO> campaignList;
}
