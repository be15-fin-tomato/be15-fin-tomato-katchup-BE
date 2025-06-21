package be15fintomatokatchupbe.campaign.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pipeline_step")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pipelineStepId;

    private Integer level;

    private String stepName;
}
