package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.dashboard.query.dto.response.SearchRatioResponse;
import be15fintomatokatchupbe.dashboard.query.external.GoogleTrendsClient;
import be15fintomatokatchupbe.dashboard.query.external.NaverDataLabClient;
import be15fintomatokatchupbe.dashboard.query.mapper.SearchRatioQueryMapper;
import be15fintomatokatchupbe.influencer.query.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SearchRatioQueryService {

    private final GoogleTrendsClient googleTrendsClient;
    private final NaverDataLabClient naverDataLabClient;
    private final YoutubeService youtubeService;
    private final SearchRatioQueryMapper searchRatioQueryMapper;

    public SearchRatioResponse getSearchRatioByPipelineInfluencerId(Long pipelineInfluencerId) {
        // 1. 유튜브 링크 조회 및 videoId 추출
        String youtubeLink = searchRatioQueryMapper.findYoutubeLinkByPipelineInfluencerId(pipelineInfluencerId);
        String videoId = YoutubeService.extractVideoId(youtubeLink);
        if (videoId == null) {
            throw new BusinessException(CampaignErrorCode.INVALID_YOUTUBE_LINK);
        }

        // 2. 업로드 날짜 조회
        String uploadDateString = youtubeService.getUploadDate(videoId);
        LocalDate uploadDate = LocalDate.parse(uploadDateString);

        // 3. 상품명 조회 (pipelineInfluencer → pipeline → campaign → product)
        String productName = searchRatioQueryMapper.findProductNameByPipelineInfluencerId(pipelineInfluencerId);
        if (productName == null) {
            throw new BusinessException(CampaignErrorCode.INVALID_CAMPAIGN_STATUS);
        }
        System.out.println("🎯 조회된 상품명: " + productName);

        // 4. 시작일/종료일 계산
        LocalDate startDate = uploadDate.minusDays(3);
        LocalDate tentativeEndDate = uploadDate.plusDays(3);
        LocalDate endDate = tentativeEndDate.isAfter(LocalDate.now()) ? LocalDate.now() : tentativeEndDate;

        // 5. 검색 데이터 조회
        Map<String, Integer> googleData = googleTrendsClient.getSearchRatio(productName, startDate, endDate);
        Map<String, Integer> naverData = naverDataLabClient.getSearchRatio(productName, startDate, endDate);

        return new SearchRatioResponse(googleData, naverData);
    }

    public String extractVideoIdByPipelineInfluencerId(Long pipelineInfluencerId) {
        String youtubeLink = searchRatioQueryMapper.findYoutubeLinkByPipelineInfluencerId(pipelineInfluencerId);
        String videoId = YoutubeService.extractVideoId(youtubeLink);
        if (videoId == null) {
            throw new BusinessException(CampaignErrorCode.INVALID_YOUTUBE_LINK);
        }
        return videoId;
    }
}
