package be15fintomatokatchupbe.calendar.query.dto.schedule;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ScheduleListResponse {

    private final List<ScheduleListDTO> scheduleList;

}
