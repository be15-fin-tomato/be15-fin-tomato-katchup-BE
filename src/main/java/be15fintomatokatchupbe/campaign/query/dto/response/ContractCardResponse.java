package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ContractCardResponse extends CardResponse{
    private String productName;
    private Long expectedRevenue;
}
