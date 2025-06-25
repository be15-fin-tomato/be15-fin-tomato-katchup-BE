package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.response.common.CardResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class QuotationCardResponse extends CardResponse {
    private String productName;
    private Long expectedRevenue;
}
