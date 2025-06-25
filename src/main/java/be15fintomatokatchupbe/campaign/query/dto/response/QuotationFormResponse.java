package be15fintomatokatchupbe.campaign.query.dto.response;


import be15fintomatokatchupbe.campaign.query.dto.response.common.PipelineFormResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class QuotationFormResponse extends PipelineFormResponse {
    List<InfluencerInfo> influencerList;

    // 견적가, 공급가능수량, 기대수익
    private Long expectedRevenue;
    private Long availableQuantity;
    private Long expectedProfit;

}
