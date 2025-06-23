package be15fintomatokatchupbe.calendar.query.service;

import be15fintomatokatchupbe.calendar.query.dto.ScheduleListDTO;
import be15fintomatokatchupbe.calendar.query.dto.ScheduleListResponse;
import be15fintomatokatchupbe.calendar.query.dto.ScheduleListsAllDTO;
import be15fintomatokatchupbe.calendar.query.dto.ScheduleListsAllResponse;
import be15fintomatokatchupbe.calendar.query.mapper.ScheduleQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarQueryService {
    private final ScheduleQueryMapper scheduleQueryMapper;

    // 날짜별 일정 조회
    public ScheduleListResponse getScheduleList(Long userId, LocalDate date) {
        List<ScheduleListDTO> scheduleDTO = scheduleQueryMapper.getScheduleList(userId, date);
        return ScheduleListResponse.builder()
                .scheduleList(scheduleDTO)
                .build();
    }

    // 모든 일정 조회
    public ScheduleListsAllResponse getScheduleListsAll(Long userId) {
        List<ScheduleListsAllDTO> scheduleAllDTO = scheduleQueryMapper.getScheduleListsAll(userId);
        return ScheduleListsAllResponse.builder()
                .scheduleListsAll(scheduleAllDTO)
                .build();
    }
}