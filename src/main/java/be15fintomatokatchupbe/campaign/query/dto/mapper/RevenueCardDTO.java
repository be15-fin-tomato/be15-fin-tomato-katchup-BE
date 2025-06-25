package be15fintomatokatchupbe.campaign.query.dto.mapper;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RevenueCardDTO {
    private Long pipelineId;
    private String name;
    private String statusName;
    private String clientCompanyName;
    private String clientManagerName;
    private String productName;
    private Long expectedRevenue;
    private String userNameInfo;
}