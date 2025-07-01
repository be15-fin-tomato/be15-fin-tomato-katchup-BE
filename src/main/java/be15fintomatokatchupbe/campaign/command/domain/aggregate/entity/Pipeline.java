package be15fintomatokatchupbe.campaign.command.domain.aggregate.entity;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pipeline")
@Getter
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
    @JoinColumn(name = "pipeline_status_id")
    private PipelineStatus pipelineStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

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

    private Long availableQuantity;

    private Long totalProfit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isDeleted = StatusType.N;

    public void updateQuotation(PipelineStatus pipelineStatus,
                                User writer,
                                String name,
                                LocalDateTime requestAt,
                                LocalDateTime startedAt,
                                LocalDateTime endedAt,
                                LocalDateTime presentedAt,
                                Campaign campaign,
                                String content,
                                String notes,
                                Long expectedRevenue,
                                Long expectedProfit,
                                Long availableQuantity) {

        this.pipelineStatus = pipelineStatus;
        this.writer = writer;
        this.name = name;
        this.requestAt = requestAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.presentedAt = presentedAt;
        this.campaign = campaign;
        this.content = content;
        this.notes = notes;
        this.expectedRevenue = expectedRevenue;
        this.expectedProfit = expectedProfit;
        this.availableQuantity = availableQuantity;
    }

    public void updateContract(PipelineStatus pipelineStatus,
                                User writer,
                                String name,
                                LocalDateTime requestAt,
                                LocalDateTime startedAt,
                                LocalDateTime endedAt,
                                LocalDateTime presentedAt,
                                Campaign campaign,
                                String content,
                                String notes,
                                Long expectedRevenue,
                                Long expectedProfit,
                                Long availableQuantity) {

        this.pipelineStatus = pipelineStatus;
        this.writer = writer;
        this.name = name;
        this.requestAt = requestAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.presentedAt = presentedAt;
        this.campaign = campaign;
        this.content = content;
        this.notes = notes;
        this.expectedRevenue = expectedRevenue;
        this.expectedProfit = expectedProfit;
        this.availableQuantity = availableQuantity;
    }

    public void updateRevenue(
            PipelineStatus pipelineStatus,
            Campaign campaign,
            String name,
            LocalDateTime requestAt,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            LocalDateTime presentedAt,
            String content,
            String notes,
            User writer
    ){
        this.pipelineStatus = pipelineStatus;
        this.writer = writer;
        this.name = name;
        this.requestAt = requestAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.presentedAt = presentedAt;
        this.campaign = campaign;
        this.content = content;
        this.notes = notes;
    }
}
