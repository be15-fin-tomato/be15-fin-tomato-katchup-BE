package be15fintomatokatchupbe.calendar.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleListsAllDTO {

    private final Long scheduleId;
    private final Long userId;
    private final Long scheduleColorId;
    private final String content;
    private final String scheduleDate;
    private final String startTime;
    private final String endTime;

}
