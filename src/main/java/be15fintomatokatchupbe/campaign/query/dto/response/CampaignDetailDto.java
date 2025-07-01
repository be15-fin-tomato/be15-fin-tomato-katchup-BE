package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CampaignDetailDto {
    private Long campaignId;
    private String campaignName;
    private String productName;
    private Long productPrice;
    private Long salesQuantity;
    private LocalDateTime createdAt;
    private String clientCompanyName;
}