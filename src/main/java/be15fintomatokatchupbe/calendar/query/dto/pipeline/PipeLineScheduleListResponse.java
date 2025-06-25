package be15fintomatokatchupbe.calendar.query.dto.pipeline;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PipeLineScheduleListResponse {

    private final List<PipelineScheduleListDTO> pipelineScheduleList;

}