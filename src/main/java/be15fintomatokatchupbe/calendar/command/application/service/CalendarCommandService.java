package be15fintomatokatchupbe.calendar.command.application.service;

import be15fintomatokatchupbe.calendar.command.application.dto.CreateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.domain.aggregate.Schedule;
import be15fintomatokatchupbe.calendar.command.mapper.ScheduleCommandMapper;
import be15fintomatokatchupbe.calendar.command.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CalendarCommandService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleCommandMapper mapper;

//    @Transactional
//    public void create(Long userId, CreateScheduleRequestDto dto) {
//        Schedule schedule = mapper.toEntity(dto, userId);
//        scheduleRepository.save(schedule);
//    }
}