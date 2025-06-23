package be15fintomatokatchupbe.calendar.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class ScheduleListsAllDTO {

    private final Long scheduleId;
    private final Long userId;
    private final Long scheduleColorId;
    private final String scheduleColorName;
    private final String hexCode;
    private final String content;
    private final LocalDate scheduleDate;
    private final LocalTime startTime;
    private final LocalTime endTime;

}
