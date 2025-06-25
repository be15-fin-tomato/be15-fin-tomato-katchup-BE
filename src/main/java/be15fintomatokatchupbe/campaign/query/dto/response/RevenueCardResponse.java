package be15fintomatokatchupbe.campaign.query.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RevenueCardResponse extends CardResponse{
    private String productName;
    private Long expectedRevenue;
}
