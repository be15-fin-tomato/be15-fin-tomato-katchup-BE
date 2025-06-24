package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
public class CreateQuotationRequest extends BasePipelineRequest {
    // 인플루언서 정보
    private List<Long> influencerId;
    private List<IdeaRequest> ideaList;

    // 견적가, 공급가능수량, 기대수익
    private Long expectedRevenue;
    private Long availableQuantity;
    private Long expectedProfit;
}
