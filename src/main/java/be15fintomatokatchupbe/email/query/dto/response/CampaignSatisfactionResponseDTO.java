package be15fintomatokatchupbe.email.query.dto.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CampaignSatisfactionResponseDTO {

    private double campaignResponseRate;
    private transient double campaignUnResponseRate;

    public double getCampaignUnResponseRate() {
        return 100 - campaignResponseRate;
    }

}
