package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfluencerProposalInfo {
    private Long influencerId;
    private String influencerName;
    private String strength;
    private String notes;
}
