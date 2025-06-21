package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class TodayScheduleResponse {
    private String content;
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String hexCode;
}
