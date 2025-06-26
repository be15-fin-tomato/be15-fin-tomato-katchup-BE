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

    public SearchRatioResponse getSearchRatioByCampaignId(Long campaignId) {
        // 1. 캠페인 상태 확인 및 상품명 조회
        String productName = searchRatioQueryMapper.findProductNameByCampaignId(campaignId);
        if (productName == null) {
            throw new BusinessException(CampaignErrorCode.INVALID_CAMPAIGN_STATUS);
        }

        // 2. pipeline_id 조회
        Long pipelineId = searchRatioQueryMapper.findPipelineIdByCampaignId(campaignId);
        if (pipelineId == null) {
            throw new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND);
        }

        // 3. 유튜브 링크 조회 후 videoId 추출
        String youtubeLink = searchRatioQueryMapper.findYoutubeLinkByPipelineId(pipelineId);
        String videoId = YoutubeService.extractVideoId(youtubeLink);
        if (videoId == null) {
            throw new BusinessException(CampaignErrorCode.INVALID_YOUTUBE_LINK);
        }

        // 4. 업로드 날짜 조회
        String uploadDateString = youtubeService.getUploadDate(videoId);
        LocalDate uploadDate = LocalDate.parse(uploadDateString);

        // 5. 시작일/종료일 계산 (오늘보다 미래로 가면 안 됨)
        LocalDate startDate = uploadDate.minusDays(3);
        LocalDate tentativeEndDate = uploadDate.plusDays(3);
        LocalDate endDate = tentativeEndDate.isAfter(LocalDate.now()) ? LocalDate.now() : tentativeEndDate;

        // 6. 검색량 데이터 조회
        Map<String, Integer> googleData = googleTrendsClient.getSearchRatio(productName, startDate, endDate);
        Map<String, Integer> naverData = naverDataLabClient.getSearchRatio(productName, startDate, endDate);

        return new SearchRatioResponse(googleData, naverData);
    }
}
