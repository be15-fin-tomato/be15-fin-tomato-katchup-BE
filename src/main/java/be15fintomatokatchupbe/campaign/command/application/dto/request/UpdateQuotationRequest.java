package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Getter
public class UpdateQuotationRequest extends BasePipelineRequest{
    // 수정할 파이프라인 이름
    private Long pipelineId;

    // 인플루언서 정보
    private List<Long> influencerId;

    private Long expectedRevenue;
    private Long availableQuantity;
    private Long expectedProfit;
}
