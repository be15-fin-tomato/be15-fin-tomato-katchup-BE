package be15fintomatokatchupbe.calendar.command.domain.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "pipeline")
@Getter
@NoArgsConstructor
public class PipelineSchedule {

    @Id
    private Long pipelineId;

    private String name;
    private Date startedAt;
    private Date endedAt;

    @Builder
    public PipelineSchedule(Long pipelineId, String name, Date startedAt, Date endedAt) {
        this.pipelineId = pipelineId;
        this.name = name;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
