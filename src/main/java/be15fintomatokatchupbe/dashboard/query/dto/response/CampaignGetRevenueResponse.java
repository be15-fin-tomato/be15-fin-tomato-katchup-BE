package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Builder
@Getter
public class CampaignGetRevenueResponse {
    List<CampaignGetRevenueDTO> campaignGetRevenue;
}
