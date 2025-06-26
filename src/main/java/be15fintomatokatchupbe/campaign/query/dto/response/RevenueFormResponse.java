package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.response.common.PipelineFormResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RevenueFormResponse extends PipelineFormResponse {
    List<InfluencerRevenueInfo> influencerList;

    // 광고 단가, 상품 가격, 판매 수량
    private Long

}
