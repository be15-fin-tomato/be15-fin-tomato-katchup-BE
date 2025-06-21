package be15fintomatokatchupbe.campaign.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pipeline_status")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineStatus {

    @Id
    private Long pipelineStatusId;

    private String statusName;
}
