package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ListupDetailResponse {
    private final Long campaignId;
    private final String campaignName;
    private final Long clientCompanyId;
    private final String clientCompanyName;
    private final List<InfluencerInfo> influencerList;
}