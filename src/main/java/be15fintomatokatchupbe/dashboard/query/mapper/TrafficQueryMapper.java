package be15fintomatokatchupbe.dashboard.query.mapper;

import be15fintomatokatchupbe.dashboard.query.dto.response.TrafficResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrafficQueryMapper {

    List<TrafficResponse> getTrafficByPipelineInfluencer(@Param("pipelineInfluencerId") Long pipelineInfluencerId);
}
