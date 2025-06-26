package be15fintomatokatchupbe.dashboard.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SearchRatioQueryMapper {

    @Select("SELECT product_name FROM campaign WHERE campaign_id = #{campaignId}")
    String findProductNameByCampaignId(Long campaignId);

    @Select("SELECT pipeline_id FROM pipeline WHERE campaign_id = #{campaignId} LIMIT 1")
    Long findPipelineIdByCampaignId(Long campaignId);

    @Select("SELECT youtube_link FROM pipeline_influencer_client_manager WHERE pipeline_id = #{pipelineId} LIMIT 1")
    String findYoutubeLinkByPipelineId(Long pipelineId);
}



