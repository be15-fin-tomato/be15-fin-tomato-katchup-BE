package be15fintomatokatchupbe.calendar.command.mapper;

import be15fintomatokatchupbe.calendar.command.application.dto.request.CreateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.domain.aggregate.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCommandMapper {
    public Schedule toEntity(CreateScheduleRequestDto dto, Long userId) {
        return Schedule.builder()
                .userId(userId)
                .scheduleDate(dto.getScheduleDate())
                .content( dto.getContent())
                .startTime( dto.getStartTime())
                .endTime( dto.getEndTime())
                .scheduleColorId(dto.getScheduleColorId())
                .build();
    }
}
