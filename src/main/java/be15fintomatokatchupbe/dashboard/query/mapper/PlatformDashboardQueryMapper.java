package be15fintomatokatchupbe.dashboard.query.mapper;

import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlatformDashboardQueryMapper {
    List<CampaignResponse> findCampaignsByInfluencerId(Long influencerId);
}
