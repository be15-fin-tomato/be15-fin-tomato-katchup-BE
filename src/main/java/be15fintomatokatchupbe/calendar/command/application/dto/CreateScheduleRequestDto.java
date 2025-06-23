package be15fintomatokatchupbe.calendar.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateScheduleRequestDto {

    private Long userId;
    private LocalDate scheduleDate;
    private String content;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long scheduleColorId;
}
