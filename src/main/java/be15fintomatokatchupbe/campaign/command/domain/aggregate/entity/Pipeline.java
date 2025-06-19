package be15fintomatokatchupbe.campaign.command.domain.aggregate.entity;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    @JoinColumn(nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private PipelineStep pipelineStep;

    @ManyToOne(fetch = FetchType.LAZY)
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    private PipelineStatus pipelineStatus;

    private String title;

    private Timestamp startedAt;

    private Timestamp endedAt;

    private Timestamp presentedAt;

    private String notes;

    private Long expectedRevenue;

    private BigDecimal expectedProfitMargin;

    private Timestamp requestAt;

    private String content;

    private Long expectedProfit;

    private Long totalProfit;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isDeleted = StatusType.N;
}
