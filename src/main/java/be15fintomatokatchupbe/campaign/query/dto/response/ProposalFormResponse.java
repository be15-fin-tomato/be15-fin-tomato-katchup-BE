package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.response.common.PipelineFormResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class ProposalFormResponse extends PipelineFormResponse {
    List<InfluencerProposalInfo> influencerList;
}
