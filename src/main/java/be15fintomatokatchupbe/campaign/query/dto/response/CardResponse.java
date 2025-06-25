package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class CardResponse {
    private Long pipelineId;    // 파이프라인 ID
    private String name;    // 파이프라인 이름
    private String statusName;    // 파이프라인 상태
    private String clientCompanyName;   // 고객사 명
    private String clientManagerName;   // 광고 담당자명
    private List<String> userName;    // 담당자 명
}
