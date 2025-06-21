package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class CreateProposalRequest extends BasePipelineRequest{
    private List<InfluencerProposalRequest> influencerList;
}
