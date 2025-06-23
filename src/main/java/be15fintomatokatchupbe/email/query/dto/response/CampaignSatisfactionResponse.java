package be15fintomatokatchupbe.email.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CampaignSatisfactionResponse {
    private final List<CampaignSatisfactionDTO> campaignSatisfaction;
    private final Pagination pagination;
}