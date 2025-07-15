package be15fintomatokatchupbe.calendar.query.dto.pipeline;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PipelineScheduleListDTO {

    private final Long pipelineId;
    private final Long campaignId;
    private final Long userId;
    private final String name;
    private final LocalDate startedAt;
    private final LocalDate endedAt;
    private final LocalDate presentedAt;

    public enum DeleteStatus {
        Y, N
    }
    private final DeleteStatus isDeleted;
}