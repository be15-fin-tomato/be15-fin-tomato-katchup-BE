package be15fintomatokatchupbe.calendar.query.mapper;

import be15fintomatokatchupbe.calendar.query.dto.pipeline.PipelineScheduleListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PipelineScheduleQueryMapper {

    List<PipelineScheduleListDTO> getPipelineScheduleList();

}