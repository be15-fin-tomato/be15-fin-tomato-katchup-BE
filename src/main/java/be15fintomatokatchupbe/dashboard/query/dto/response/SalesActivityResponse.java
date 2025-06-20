package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalesActivityResponse {
    private int clientCompanyCount;
    private int influencerCount;
    private int contractCount;
    private int pipelineCount;
}

