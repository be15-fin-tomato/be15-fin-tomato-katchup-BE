package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class CampaignDetailWithTimelineResponse {
    private CampaignDetailDto campaignDetail;
    private List<PipelineTimelineDto> timeline;
}