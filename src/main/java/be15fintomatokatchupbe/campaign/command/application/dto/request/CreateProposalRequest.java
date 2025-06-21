package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
public class CreateProposalRequest extends BasePipelineRequest{
    private List<InfluencerProposalRequest> influencerList;
    private List<IdeaRequest> ideaList;
}
