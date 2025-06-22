package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignResponse;
import be15fintomatokatchupbe.dashboard.query.mapper.PlatformDashboardQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatformDashboardQueryService {
    private final PlatformDashboardQueryMapper platformDashboardQueryMapper;

    public List<CampaignResponse> getCampaignList(Long influencerId) {
        return platformDashboardQueryMapper.findCampaignsByInfluencerId(influencerId);
    }
}
