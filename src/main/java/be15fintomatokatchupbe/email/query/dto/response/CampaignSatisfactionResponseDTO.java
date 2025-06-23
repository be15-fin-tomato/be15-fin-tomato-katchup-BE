package be15fintomatokatchupbe.email.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CampaignSatisfactionResponseDTO {

    private double campaignResponseRate;
}
