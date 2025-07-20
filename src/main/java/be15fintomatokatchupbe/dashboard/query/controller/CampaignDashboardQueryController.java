package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignContentResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignContentThumbnail;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignGetRevenueResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.ThumbnailResponse;
import be15fintomatokatchupbe.dashboard.query.service.CampaignDashboardQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "캠페인 성과 대시보드")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class CampaignDashboardQueryController {

    private final CampaignDashboardQueryService campaignDashboardQueryService;

    @Operation(summary = "캠페인 컨텐츠 정보 조회", description = "사용자는 진행한 캠페인에 대한 컨텐츠 분석 정보를 조회할 수 있다.")
    @GetMapping("/content/{pipelineInfluencerId}")
    public ResponseEntity<ApiResponse<CampaignContentResponse>> getCampaignContent(
            @PathVariable Long pipelineInfluencerId
    ){
        CampaignContentResponse response = campaignDashboardQueryService.getCampaignContent(pipelineInfluencerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "성과 대시보드 수익요약 조회", description = "사용자는 성과 대시보드 수익요약을 조회할 수 있다.")
    @GetMapping("/get/revenue/{pipelineInfluencerId}")
    public ResponseEntity<ApiResponse<CampaignGetRevenueResponse>> getRevenue (
            @PathVariable Long pipelineInfluencerId
    ) {
        CampaignGetRevenueResponse response = campaignDashboardQueryService.getRevenue(pipelineInfluencerId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "인플루언서 채널 썸네일 조회", description = "특정 파이프라인 인플루언서의 대표 유튜브 채널 썸네일을 조회합니다.")
    @GetMapping("/influencer-thumbnail/{pipelineInfluencerId}")
    public ResponseEntity<ApiResponse<CampaignContentThumbnail>> getInfluencerThumbnail(
            @PathVariable Long pipelineInfluencerId
    ) {

        CampaignContentThumbnail response = campaignDashboardQueryService.getInfluencerThumbnail(pipelineInfluencerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}