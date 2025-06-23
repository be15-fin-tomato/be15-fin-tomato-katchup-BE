package be15fintomatokatchupbe.calendar.query.mapper;

import be15fintomatokatchupbe.calendar.query.dto.ScheduleListsAllDTO;
import be15fintomatokatchupbe.calendar.query.dto.ScheduleListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface ScheduleQueryMapper {

    List<ScheduleListDTO> getScheduleList(Long userId, Date date);

    List<ScheduleListsAllDTO> getScheduleListsAll(Long userId);
}