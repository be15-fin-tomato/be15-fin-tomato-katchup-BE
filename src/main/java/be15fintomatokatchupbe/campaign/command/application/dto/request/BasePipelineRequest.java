package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
public class BasePipelineRequest {
    private Long campaignId;
//    private Long pipelineStepId;  // 넣지 않기!
    private Long pipelineStatusId;
    private Long clientCompanyId;
    private Long clientManagerId;
    private List<Long> userId;
    private String name;
    private LocalDate requestAt;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private LocalDate presentedAt;
    private String campaignName;
    private String content;
    private String notes;

    // 인플루언서는 자식 클래스에서 관리하기로!
    // private List<Long> influencerId;
}