package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class InfluencerProposalRequest extends InfluencerRequest{
    private String strength;
    private String notes;
}
