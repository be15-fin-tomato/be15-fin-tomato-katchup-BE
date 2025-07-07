package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListupResponse {
    private Long pipelineId;
    private String companyName;
    private String campaignName;
    private String productName;
    private String listupTitle;
}

