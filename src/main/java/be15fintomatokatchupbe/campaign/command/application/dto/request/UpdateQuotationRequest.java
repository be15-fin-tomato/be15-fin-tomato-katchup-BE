package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class UpdateQuotationRequest extends BasePipelineRequest{
    // 인플루언서 정보
    private List<Long> influencerId;

    private Long expectedRevenue;
    private Long availableQuantity;
    private Long expectedProfit;
}
