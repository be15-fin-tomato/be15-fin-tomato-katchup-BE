package be15fintomatokatchupbe.influencer.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class CampaignRecord {
    private final Long pipelineId;
    private final String campaignName;
}
