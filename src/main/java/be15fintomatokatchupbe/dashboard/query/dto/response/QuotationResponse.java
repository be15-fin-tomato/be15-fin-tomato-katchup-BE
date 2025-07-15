package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuotationResponse {
    private Long pipelineId;
    private String companyName;
    private String campaignName;
    private String productName;
    private String quotationTitle;
    private Long expectedRevenue;
    private Long expectedProfit;
    private LocalDateTime presentedAt;
}
