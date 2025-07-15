package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.mapper.CampaignWithCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignAiResponse {
    List<CampaignWithCategoryDTO> campaignList;
}
