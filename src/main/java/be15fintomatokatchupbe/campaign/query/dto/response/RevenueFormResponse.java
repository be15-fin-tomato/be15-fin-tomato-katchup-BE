package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.response.common.PipelineFormResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class RevenueFormResponse extends PipelineFormResponse {
    List<InfluencerRevenueInfo> influencerList;

    // 상품 가격, 판매 수량
    private Long productPrice;
    private Long salesQuantity;
}
