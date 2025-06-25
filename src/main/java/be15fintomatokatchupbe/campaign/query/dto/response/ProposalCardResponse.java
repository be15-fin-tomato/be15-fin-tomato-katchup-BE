package be15fintomatokatchupbe.campaign.query.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProposalCardResponse {
    private Long pipelineId;    // 파이프라인 ID
    private String name;    // 파이프라인 이름
    private String statusId;    // 파이프라인 상태
    private String clientCompanyName;   // 고객사 명
    private String clientManagerName;   // 광고 담당자명
    private List<String> userName;    // 담당자 명
    private String  requestAt;   // 요청일
    private String presentAt;   // 발표일
}
