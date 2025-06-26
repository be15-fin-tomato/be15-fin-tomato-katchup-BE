package be15fintomatokatchupbe.dashboard.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CampaignDashboardQueryMapper {

    @Select("SELECT metric_name, metric_value FROM campaign_metrics WHERE campaign_id = #{campaignId}")
    List<Map<String, Object>> findMetricsByCampaignId(Long campaignId);

    @Select("SELECT pipeline_id FROM pipeline WHERE campaign_id = #{campaignId} LIMIT 1")
    Long findPipelineIdByCampaignId(Long campaignId);

    @Select("SELECT youtube_link FROM pipeline_influencer_client_manager WHERE pipeline_id = #{pipelineId} LIMIT 1")
    String findYoutubeLinkByPipelineId(Long pipelineId);

}
