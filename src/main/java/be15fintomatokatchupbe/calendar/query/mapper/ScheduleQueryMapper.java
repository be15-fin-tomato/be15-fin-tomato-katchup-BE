package be15fintomatokatchupbe.calendar.query.mapper;

import be15fintomatokatchupbe.calendar.query.dto.ScheduleListsAllDTO;
import be15fintomatokatchupbe.calendar.query.dto.ScheduleListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalTime;
import java.util.List;

@Mapper
public interface ScheduleQueryMapper {

    List<ScheduleListDTO> getScheduleList(Long userId, LocalTime startTime);

    List<ScheduleListsAllDTO> getScheduleListsAll(Long userId);
}