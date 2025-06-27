package be15fintomatokatchupbe.dashboard.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CampaignDashboardQueryMapper {

    @Select("SELECT youtube_link " +
            "FROM pipeline_influencer_client_manager " +
            "WHERE pipeline_id = #{pipelineId} limit 1")
    String findYoutubeLinkByPipelineId(Long pipelineId);

    @Select("SELECT picm.pipeline_id " +
            "FROM pipeline_influencer_client_manager picm " +
            "JOIN pipeline p ON picm.pipeline_id = p.pipeline_id " +
            "WHERE p.campaign_id = #{campaignId} AND picm.influencer_id = #{influencerId} limit 1")

    Long findPipelineIdByCampaignIdAndInfluencerId(Long campaignId, Long influencerId);

}