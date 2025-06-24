package be15fintomatokatchupbe.calendar.command.application.service;

import be15fintomatokatchupbe.calendar.command.application.dto.CreateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.domain.aggregate.Schedule;
import be15fintomatokatchupbe.calendar.command.mapper.ScheduleCommandMapper;
import be15fintomatokatchupbe.calendar.command.repository.ScheduleRepository;
import be15fintomatokatchupbe.calendar.exception.CalendarErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CalendarCommandService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleCommandMapper mapper;

    @Transactional
    public void create(Long userId, CreateScheduleRequestDto dto) {
        Schedule schedule = mapper.toEntity(dto, userId);

        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }

        scheduleRepository.save(schedule);
    }

    @Transactional
    public void delete(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByScheduleIdAndUserId(scheduleId, userId)
                .orElseThrow(() -> new BusinessException(CalendarErrorCode.ACCESS_DENIED));
        scheduleRepository.delete(schedule);
    }
}