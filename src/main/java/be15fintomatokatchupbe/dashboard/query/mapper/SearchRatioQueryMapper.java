package be15fintomatokatchupbe.dashboard.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SearchRatioQueryMapper {

    // 기존 (campaignId 기반)
    @Select("SELECT product_name FROM campaign WHERE campaign_id = #{campaignId}")
    String findProductNameByCampaignId(Long campaignId);

    @Select("SELECT pipeline_id FROM pipeline WHERE campaign_id = #{campaignId} AND pipeline_step_id = 7")
    Long findPipelineIdByCampaignId(Long campaignId);

    @Select("SELECT youtube_link FROM pipeline_influencer_client_manager WHERE pipeline_id = #{pipelineId} LIMIT 1")
    String findYoutubeLinkByPipelineId(Long pipelineId);

    // ✅ 추가: pipelineInfluencerId → 유튜브 링크
    @Select("SELECT youtube_link FROM pipeline_influencer_client_manager WHERE pipeline_influencer_id = #{pipelineInfluencerId}")
    String findYoutubeLinkByPipelineInfluencerId(Long pipelineInfluencerId);

    // ✅ 추가: pipelineInfluencerId → product_name
    @Select("""
        SELECT c.product_name
        FROM pipeline_influencer_client_manager picm
        JOIN pipeline p ON picm.pipeline_id = p.pipeline_id
        JOIN campaign c ON p.campaign_id = c.campaign_id
        WHERE picm.pipeline_influencer_id = #{pipelineInfluencerId}
    """)
    String findProductNameByPipelineInfluencerId(Long pipelineInfluencerId);
}
