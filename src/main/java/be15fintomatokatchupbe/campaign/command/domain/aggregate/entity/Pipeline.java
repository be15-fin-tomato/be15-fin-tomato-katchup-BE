package be15fintomatokatchupbe.campaign.command.domain.aggregate.entity;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pipeline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pipelineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_step_id", nullable = false)
    private PipelineStep pipelineStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_status_id")
    private PipelineStatus pipelineStatus;

    private String name;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private LocalDateTime presentedAt;

    private String notes;

    private Long expectedRevenue;

    private BigDecimal expectedProfitMargin;

    private LocalDateTime requestAt;

    private String content;

    private Long expectedProfit;

    private Long totalProfit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isDeleted = StatusType.N;
}
