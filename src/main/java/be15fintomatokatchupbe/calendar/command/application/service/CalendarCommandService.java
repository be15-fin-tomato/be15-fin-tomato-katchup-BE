package be15fintomatokatchupbe.calendar.command.application.service;

import be15fintomatokatchupbe.calendar.command.application.dto.request.CreateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.application.dto.request.UpdateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.domain.aggregate.Schedule;
import be15fintomatokatchupbe.calendar.command.mapper.ScheduleCommandMapper;
import be15fintomatokatchupbe.calendar.command.repository.ScheduleRepository;
import be15fintomatokatchupbe.calendar.exception.CalendarErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class CalendarCommandService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleCommandMapper mapper;

    @Transactional
    public void create(Long userId, CreateScheduleRequestDto dto) {
        Schedule schedule = mapper.toEntity(dto, userId);

        LocalTime startTime = dto.getStartTime() != null ? dto.getStartTime() : schedule.getStartTime();
        LocalTime endTime = dto.getEndTime() != null ? dto.getEndTime() : schedule.getEndTime();

        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new BusinessException(CalendarErrorCode.INVALID_TIME);
        }

        scheduleRepository.save(schedule);
    }

    @Transactional
    public void update(Long userId, Long scheduleId, UpdateScheduleRequestDto dto) {
        Schedule schedule = scheduleRepository.findByScheduleIdAndUserId(scheduleId, userId)
                .orElseThrow(() -> new BusinessException(CalendarErrorCode.ACCESS_DENIED));

        LocalTime startTime = dto.getStartTime() != null ? dto.getStartTime() : schedule.getStartTime();
        LocalTime endTime = dto.getEndTime() != null ? dto.getEndTime() : schedule.getEndTime();

        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new BusinessException(CalendarErrorCode.INVALID_TIME);
        }
        schedule.update(dto);
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void delete(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByScheduleIdAndUserId(scheduleId, userId)
                .orElseThrow(() -> new BusinessException(CalendarErrorCode.ACCESS_DENIED));
        scheduleRepository.delete(schedule);
    }

}