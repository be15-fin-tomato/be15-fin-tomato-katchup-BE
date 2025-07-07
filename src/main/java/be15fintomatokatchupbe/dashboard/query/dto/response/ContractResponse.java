package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContractResponse {
    private Long pipelineId;
    private String companyName;
    private String campaignName;
    private String productName;
    private String contractTitle;
    private Long expectedRevenue;
    private Long expectedProfit;
    private String statusName;
    private LocalDateTime createdAt;
}
