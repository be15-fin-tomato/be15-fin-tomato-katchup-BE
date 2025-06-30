package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignContentResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignGetRevenueDTO;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignGetRevenueResponse;
import be15fintomatokatchupbe.dashboard.query.mapper.CampaignDashboardQueryMapper;
import be15fintomatokatchupbe.influencer.query.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CampaignDashboardQueryService {

    private final CampaignDashboardQueryMapper mapper;
    private final YoutubeService youtubeService;

    public CampaignContentResponse getCampaignContent(Long campaignId, Long influencerId) {
        // 1. pipelineId 조회
        Long pipelineId = mapper.findPipelineIdByCampaignIdAndInfluencerId(campaignId, influencerId);
        if (pipelineId == null) {
            throw new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND);
        }

        // 2. 유튜브 링크 조회
        String youtubeLink = mapper.findYoutubeLinkByPipelineId(pipelineId);
        if (youtubeLink == null || youtubeLink.isEmpty()) {
            throw new BusinessException(CampaignErrorCode.INVALID_YOUTUBE_LINK);
        }

        // 3. videoId 추출
        String videoId = YoutubeService.extractVideoId(youtubeLink);

        // 4. 유튜브 메트릭 조회
        Map<String, Long> metrics = youtubeService.getVideoMetrics(videoId);

        // 5. 응답 객체 생성
        return new CampaignContentResponse(metrics);
    }

    public CampaignGetRevenueResponse getRevenue(Long campaignId, Long influencerId) {

        List<CampaignGetRevenueDTO> response = mapper.getRevenue(campaignId, influencerId);

        return CampaignGetRevenueResponse.builder()
                .campaignGetRevenue(response)
                .build();
    }
}