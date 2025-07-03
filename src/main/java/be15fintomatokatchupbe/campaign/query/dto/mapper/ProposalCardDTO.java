package be15fintomatokatchupbe.campaign.query.dto.mapper;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalCardDTO {
    private Long pipelineId;    // 파이프라인 ID
    private String name;    // 파이프라인 이름
    private String campaignId;  // 캠페인 ID
    private String campaignName; //캠페인 명
    private Long statusId;  // 파이프라인 상태 ID
    private String statusName;    // 파이프라인 상태명
    private Long clientCompanyId;   // 고객사 ID
    private String clientCompanyName;   // 고객사 명
    private String clientManagerId; // 광고 담당자 ID
    private String clientManagerName;   // 광고 담당자명
    private String  requestAt;   // 요청일
    private String presentAt;   // 발표일
    private String userNameInfo;    // 담당자 명
}
