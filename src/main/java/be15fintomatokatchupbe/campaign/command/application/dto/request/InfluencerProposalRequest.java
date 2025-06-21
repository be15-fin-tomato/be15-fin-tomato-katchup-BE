package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InfluencerProposalRequest extends InfluencerRequest{
    private String strength;
    private String notes;
}
