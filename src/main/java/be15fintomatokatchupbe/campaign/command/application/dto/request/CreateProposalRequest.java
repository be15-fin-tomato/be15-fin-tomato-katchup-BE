package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateProposalRequest extends BasePipelineRequest{
    private List<InfluencerProposalRequest> influencerList;
}
