package be15fintomatokatchupbe.calendar.query.service;

import be15fintomatokatchupbe.calendar.query.dto.pipeline.PipeLineScheduleListResponse;
import be15fintomatokatchupbe.calendar.query.dto.pipeline.PipelineScheduleListDTO;
import be15fintomatokatchupbe.calendar.query.dto.schedule.ScheduleListDTO;
import be15fintomatokatchupbe.calendar.query.dto.schedule.ScheduleListResponse;
import be15fintomatokatchupbe.calendar.query.dto.schedule.ScheduleListsAllDTO;
import be15fintomatokatchupbe.calendar.query.dto.schedule.ScheduleListsAllResponse;
import be15fintomatokatchupbe.calendar.query.mapper.PipelineScheduleQueryMapper;
import be15fintomatokatchupbe.calendar.query.mapper.ScheduleQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarQueryService {
    private final ScheduleQueryMapper scheduleQueryMapper;
    private final PipelineScheduleQueryMapper pipelineScheduleQueryMapper;

    // 날짜별 일정 조회
    public ScheduleListResponse getScheduleList(Long userId, Date date) {
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

    // 파이프라인 일정 불러오기
    public PipeLineScheduleListResponse getPipelineScheduleLists(Long userId) {
        List <PipelineScheduleListDTO> pipelineScheduleListDTO = pipelineScheduleQueryMapper.getPipelineScheduleList(userId);
        return PipeLineScheduleListResponse.builder()
                .pipelineScheduleList(pipelineScheduleListDTO)
                .build();
    }

}