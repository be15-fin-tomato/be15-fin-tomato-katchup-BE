package be15fintomatokatchupbe.dashboard.query.mapper;

import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignGetRevenueDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CampaignDashboardQueryMapper {

    @Select("SELECT youtube_link " +
            "FROM pipeline_influencer_client_manager " +
            "WHERE pipeline_influencer_id = #{pipelineInfluencerId} AND youtube_link IS NOT NULL " +
            "LIMIT 1")
    String findYoutubeLinkByPipelineInfluencerId(Long pipelineInfluencerId);

    @Select("SELECT pipeline_id FROM pipeline_influencer_client_manager WHERE pipeline_influencer_id = #{pipelineInfluencerId} LIMIT 1")
    Long findPipelineIdByPipelineInfluencerId(Long pipelineInfluencerId);

    List<CampaignGetRevenueDTO> getRevenue(Long pipelineInfluencerId);
}