package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BasePipelineRequest {
    private Long campaignId;
    private Long pipelineStepId;
    private Long pipelineStatusId;
    private Long clientCompanyId;
    private Long clientManagerId;
    private List<Long> userId;
    private String name;
    private LocalDateTime requestAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime presentedAt;
    private String campaignName;
    private String content;
    private String notes;

    // 인플루언서는 자식 클래스에서 관리하기로!
    // private List<Long> influencerId;
}