package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.dashboard.query.dto.response.*;
import be15fintomatokatchupbe.dashboard.query.mapper.CampaignDashboardQueryMapper;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
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

        // 1. 비디오 메트릭 가져오기
        Map<String, Long> metrics = youtubeService.getVideoMetrics(videoId);
        log.info("metrics: {}", metrics);

        // 2. 비디오 상세 정보 (제목, 설명, 발행일, 채널 ID) 가져오기
        YoutubeService.YoutubeVideoDetails videoDetails = youtubeService.fetchVideoDetails(videoId);
        String videoTitle = videoDetails != null ? videoDetails.videoTitle() : null;
        String videoDescription = videoDetails != null ? videoDetails.videoDescription() : null;
        String publishedAt = videoDetails != null ? videoDetails.publishedAt() : null;
        String channelId = videoDetails != null ? videoDetails.channelId() : null;
        log.info("videoTitle: {}, videoDescription: {}, publishedAt: {}, channelId: {}", videoTitle, videoDescription, publishedAt, channelId);


        // 3. 비디오 썸네일 URL 가져오기 (YoutubeService의 메서드 사용)
        String videoThumbnailUrl = youtubeService.getVideoThumbnailUrl(videoId);
        log.info("videoThumbnailUrl: {}", videoThumbnailUrl);

        // 4. 채널 썸네일 URL 가져오기
        String channelThumbnailUrl = null;
        if (channelId != null) {
            YoutubeService.YoutubeChannelInfo channelInfo = youtubeService.fetchChannelInfo(channelId);
            if (channelInfo != null) {
                channelThumbnailUrl = channelInfo.thumbnailUrl();
                log.info("channelThumbnailUrl: {}", channelThumbnailUrl);
            }
        }

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

    @Transactional(readOnly = true)
    public CampaignContentThumbnail getInfluencerThumbnail(Long pipelineInfluencerId) {

        CampaignContentThumbnail responseFromDb = mapper.findInfluencerChannelThumbnailByPipelineInfluencerId(pipelineInfluencerId);

        if (responseFromDb == null ||
                ((responseFromDb.getChannelId() == null || responseFromDb.getChannelId().isEmpty()) &&
                        (responseFromDb.getChannelThumbnail() == null || responseFromDb.getChannelThumbnail().isEmpty()))) {
            return CampaignContentThumbnail.builder()
                    .channelId(null)
                    .channelThumbnail(null)
                    .build();
        }

        if (responseFromDb.getChannelThumbnail() != null && !responseFromDb.getChannelThumbnail().isEmpty()) {
            return responseFromDb;
        }

        if (responseFromDb.getChannelId() == null || responseFromDb.getChannelId().isEmpty()) {

            return CampaignContentThumbnail.builder()
                    .channelId(null)
                    .channelThumbnail(null)
                    .build();
        }

        try {
            YoutubeService.YoutubeChannelInfo channelInfoFromApi = youtubeService.fetchChannelInfo(responseFromDb.getChannelId());

            if (channelInfoFromApi == null || channelInfoFromApi.thumbnailUrl() == null || channelInfoFromApi.thumbnailUrl().isEmpty()) {
                return CampaignContentThumbnail.builder()
                        .channelId(responseFromDb.getChannelId())
                        .channelThumbnail(null)
                        .build();
            }
            return CampaignContentThumbnail.builder()
                    .channelId(responseFromDb.getChannelId())
                    .channelThumbnail(channelInfoFromApi.thumbnailUrl())
                    .build();
        } catch (BusinessException e) {
            System.err.println("유튜브 API 호출 중 비즈니스 예외 발생: " + e.getMessage());
            return CampaignContentThumbnail.builder()
                    .channelId(responseFromDb.getChannelId())
                    .channelThumbnail(null)
                    .build();
        } catch (Exception e) {
            System.err.println("유튜브 API 호출 중 일반 예외 발생: " + e.getMessage());
            return CampaignContentThumbnail.builder()
                    .channelId(responseFromDb.getChannelId())
                    .channelThumbnail(null)
                    .build();
        }
    }
}