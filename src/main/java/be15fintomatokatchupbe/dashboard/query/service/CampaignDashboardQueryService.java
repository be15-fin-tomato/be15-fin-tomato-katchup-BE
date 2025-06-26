package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignContentResponse;
import be15fintomatokatchupbe.dashboard.query.mapper.CampaignDashboardQueryMapper;
import be15fintomatokatchupbe.influencer.query.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CampaignDashboardQueryService {

    private final CampaignDashboardQueryMapper mapper;
    private final YoutubeService youtubeService;

    public CampaignContentResponse getCampaignContent(Long pipelineId) {
        // 1. DB에서 캠페인에 연결된 YouTube 영상 링크 조회
        String youtubeLink = mapper.findYoutubeLinkByPipelineId(pipelineId);
        if (youtubeLink == null || youtubeLink.isEmpty()) {
            throw new BusinessException(CampaignErrorCode.INVALID_YOUTUBE_LINK);
        }

        // 2. 링크에서 videoId 추출
        String videoId = YoutubeService.extractVideoId(youtubeLink);

        // 3. YouTubeService로 영상 통계 데이터 조회
        Map<String, Long> metrics = youtubeService.getVideoMetrics(videoId);

        // 4. DTO로 반환
        return new CampaignContentResponse(metrics);
    }
}