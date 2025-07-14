package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CommunicationHistoryResponse {
    private Long id;    // 파이프라인 ID
    private Long campaignId;
    private String campaignName;

    private String pipelineStepName;
    private String pipelineTitle;
    private LocalDate pipelineCreatedAt;

    private String managerName;
    private String managerDepartment;

    private String content;
    private String notes;
    private String fileName;

}