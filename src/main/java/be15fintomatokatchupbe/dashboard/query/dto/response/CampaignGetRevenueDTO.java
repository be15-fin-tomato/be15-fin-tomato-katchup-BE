package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CampaignGetRevenueDTO {

    // 판매수, 평균단가, 총이익, 예상수익, roas, 전환율
    private String salesQuantity; // c.판매수량 -> campaign

    private String averageUnitPrice; //  p.총수익 / c.판매수량

    private Long totalProfit; // c.파이프라인 단계가 계약인것에서 totalProfit

    private Long roasPercentage; // p.총수익 / (p.판매수량 * p.상품가격) * 100

    private Long expectedProfit;

}
