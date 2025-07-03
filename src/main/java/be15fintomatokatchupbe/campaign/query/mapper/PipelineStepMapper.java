package be15fintomatokatchupbe.campaign.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.response.PipelineStepStatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PipelineStepMapper {
    List<PipelineStepStatusDto> findPipelineStepsByCampaignId(@Param("campaignId") Long campaignId);
}
