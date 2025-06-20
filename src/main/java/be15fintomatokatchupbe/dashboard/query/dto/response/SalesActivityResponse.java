package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesActivityResponse {
    private int clientCompanyCount;
    private int influencerCount;
    private int contractCount;
    private int pipelineCount;
}

