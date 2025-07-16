package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignContentResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignGetRevenueDTO;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignGetRevenueResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.ThumbnailResponse;
import be15fintomatokatchupbe.dashboard.query.mapper.CampaignDashboardQueryMapper;
import be15fintomatokatchupbe.influencer.query.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignDashboardQueryService {

    private final CampaignDashboardQueryMapper mapper;
    private final YoutubeService youtubeService;

    private String buildYoutubeThumbnailUrl(String videoId) {
        if (videoId == null || videoId.trim().isEmpty()) {
            return null;
        }
        return "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";
    }

    @Transactional(readOnly = true)
    public CampaignContentResponse getCampaignContent(Long pipelineInfluencerId) {
        Long pipelineId = mapper.findPipelineIdByPipelineInfluencerId(pipelineInfluencerId);
        log.info("pipelineId: {}", pipelineId);
        if (pipelineId == null) {
            throw new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND);
        }

        String youtubeLink = mapper.findYoutubeLinkByPipelineInfluencerId(pipelineInfluencerId);
        log.info("youtubeLink: {}", youtubeLink);
        if (youtubeLink == null || youtubeLink.isEmpty()) {
            throw new BusinessException(CampaignErrorCode.INVALID_YOUTUBE_LINK);
        }
        String videoId = YoutubeService.extractVideoId(youtubeLink);
        log.info("videoId: {}", videoId);
        Map<String, Long> metrics = youtubeService.getVideoMetrics(videoId);
        log.info("metrics: {}", metrics);
        String videoThumbnailUrl = buildYoutubeThumbnailUrl(videoId);
        log.info("videoThumbnailUrl: {}", videoThumbnailUrl);
        return CampaignContentResponse.builder()
                .youtubeVideoId(videoId)
                .videoThumbnailUrl(videoThumbnailUrl)
                .metrics(metrics)
                .build();
    }


    @Transactional
    public CampaignGetRevenueResponse getRevenue(Long pipelineInfluencerId) {
        List<CampaignGetRevenueDTO> response = mapper.getRevenue(pipelineInfluencerId);

        return CampaignGetRevenueResponse.builder()
                .campaignGetRevenue(response)
                .build();
    }

}