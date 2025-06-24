package be15fintomatokatchupbe.calendar.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class UpdateScheduleRequestDto {

    private LocalDate scheduleDate;

    private String content;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long scheduleColorId;

}