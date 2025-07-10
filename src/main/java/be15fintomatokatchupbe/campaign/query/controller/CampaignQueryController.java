package be15fintomatokatchupbe.campaign.query.controller;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Campaign;
import be15fintomatokatchupbe.campaign.query.dto.request.CampaignResultRequest;
import be15fintomatokatchupbe.campaign.query.dto.request.ContractListRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.*;
import be15fintomatokatchupbe.campaign.query.dto.request.PipelineSearchRequest;
import be15fintomatokatchupbe.campaign.query.service.CampaignQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="캠페인")
@RestController
@AllArgsConstructor
@RequestMapping("/campaign")
@Tag(name = "CampaignQueryController", description = "캠페인 관련 조회 API")
public class CampaignQueryController {
    private final CampaignQueryService campaignQueryService;

    @GetMapping("/listup")
    @Operation(summary = "리스트업 목록 조회", description = "리스트업 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ListupSearchResponse>> getListupList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @ModelAttribute PipelineSearchRequest request
    ){
        Long userId = userDetail.getUserId();
        ListupSearchResponse response = campaignQueryService.getListupList(userId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/proposal")
    @Operation(summary = "제안 목록 조회", description = "제안 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ProposalSearchResponse>> getProposalList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @ModelAttribute PipelineSearchRequest request
            ){
        Long userId = userDetail.getUserId();
        ProposalSearchResponse response = campaignQueryService.getProposalList(userId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/quotation")
    @Operation(summary = "견적 목록 조회", description = "견적 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<QuotationSearchResponse>> getQuotationList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @ModelAttribute PipelineSearchRequest request
    ){
        Long userId = userDetail.getUserId();
        QuotationSearchResponse response = campaignQueryService.getQuotationList(userId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/listup/{id}")
    @Operation(summary = "리스트업 조회", description = "리스트업 상세 조회를 합니다.")
    public ResponseEntity<ApiResponse<ListupDetailResponse>> getListupDetail(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable("id") Long pipelineId
    ){
        Long userId = userDetail.getUserId();
        ListupDetailResponse response = campaignQueryService.getListupDetail(userId, pipelineId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/quotation/{id}")
    @Operation(summary = "견적 상세 조회", description = "견적 상세 조회를 합니다.")
    public ResponseEntity<ApiResponse<QuotationDetailResponse>> getQuotationDetail(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable("id") Long pipelineId
    ){
        Long userId = userDetail.getUserId();
        QuotationDetailResponse response = campaignQueryService.getQuotationDetail(userId, pipelineId);

        return ResponseEntity.ok(ApiResponse.success(response));

    }

    @GetMapping("/quotation/reference")
    @Operation(summary = "견적 참조 조회", description = "참조할 수 있는 견적 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<QuotationReferenceListResponse>> getQuotationReferenceList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(value = "campaignId", required = false) Long campaignId
    ){
        QuotationReferenceListResponse response = campaignQueryService.getQuotationReferenceList(campaignId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/contract")
    @Operation(summary = "계약 목록 조회", description = "계약 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ContractSearchResponse>> getContractList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @ModelAttribute PipelineSearchRequest request
    ){
        Long userId = userDetail.getUserId();
        ContractSearchResponse response = campaignQueryService.getContractList(userId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/contract/{id}")
    @Operation(summary = "계약 상세 조회", description = "계약 상세 조회를 합니다.")
    public ResponseEntity<ApiResponse<ContractDetailResponse>> getContractDetail(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable("id") Long pipelineId
    ){
        Long userId = userDetail.getUserId();
        ContractDetailResponse response = campaignQueryService.getContractDetail(userId, pipelineId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/contract/reference")
    @Operation(summary = "계약 참조 조회", description = "참조할 수 있는 계약 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ContractReferenceListResponse>> getContractReferenceList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(value = "campaignId", required = false) Long campaignId
    ){
        ContractReferenceListResponse response = campaignQueryService.getContractReferenceList(campaignId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/revenue")
    @Operation(summary = "매출 목록 조회", description = "매출 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<RevenueSearchResponse>> getRevenueList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @ModelAttribute PipelineSearchRequest request
    ){
        Long userId = userDetail.getUserId();
        RevenueSearchResponse response = campaignQueryService.getRevenueList(userId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/revenue/{id}")
    @Operation(summary = "매출 상세 조회", description = "매출 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<RevenueDetailResponse>> getRevenueDetail(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable("id") Long pipelineId
    ){
        Long userId = userDetail.getUserId();
        RevenueDetailResponse response = campaignQueryService.getRevenueDetail(userId, pipelineId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 캠페인 상세 및 타임라인 조회
    @GetMapping("/{campaignId}")
    @Operation(summary = "캠페인 상세 및 타임라인 조회", description = "캠페인의 기본 정보와 타임라인 정보를 함께 조회합니다.")
    public ResponseEntity<ApiResponse<CampaignDetailWithTimelineResponse>> getCampaignDetailWithTimeline(
            @PathVariable("campaignId") Long campaignId
    ) {
        CampaignDetailWithTimelineResponse response = campaignQueryService.getCampaignDetailWithTimeline(campaignId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 캠페인 목록 조회
    @GetMapping
    @Operation(summary = "캠페인 목록 조회", description = "캠페인을 페이징 형태로 조회합니다.")
    public ResponseEntity<ApiResponse<CampaignListResponse>> getCampaignList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute ContractListRequest request
    ) {
        CampaignListResponse result = campaignQueryService.getPagedCampaigns(page, size, request);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/by-client-company/{clientCompanyId}")
    @Operation(summary = "고객사 ID로 캠페인 목록 조회", description = "해당 고객사 ID로 진행된 캠페인 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CampaignListDTO>>> getCampaignsByClientCompanyId(
            @PathVariable Long clientCompanyId
    ) {
        List<CampaignListDTO> result = campaignQueryService.getCampaignsByClientCompanyId(clientCompanyId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }


    // AI 캠페인 조회
    @GetMapping("/ai/search")
    @Operation(summary = "AI 페이지 에서 사용할 캠페인 목록 조회", description = "캠페인을 검색합니다.")
    public ResponseEntity<ApiResponse<CampaignAiResponse>> getCampaignWithCategoryList(
            @RequestParam(required = false) Long clientCompanyId,
            @RequestParam(required = false) String campaignName,
            @RequestParam(required = false) List<Long> tags
    ){
        CampaignAiResponse response = campaignQueryService.getCampaignWithCategory(clientCompanyId, campaignName, tags);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "캠페인 검색", description = "캠페인을 키워드로 검색합니다.")
    public ResponseEntity<ApiResponse<CampaignSearchResponse>> findCampaignList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long clientCompanyId
    ){
        CampaignSearchResponse response = campaignQueryService.findCampaignList(keyword, clientCompanyId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/resultlist")
    @Operation(summary = "성과 목록 조회", description = "완료된 캠페인 목록을 조회할 수 있다. 검색, 필터, 정렬, 페이지네이션 기능을 모두 지원한다.")
    public ResponseEntity<ApiResponse<CampaignResultListResponse>> getCampaignResults(
            @ModelAttribute CampaignResultRequest request) {
        CampaignResultListResponse response = campaignQueryService.findCampaignResultList(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

