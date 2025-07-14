package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CampaignListResponse {
    List<CampaignListsDTO> campaignList;
    Pagination pagination;
}
