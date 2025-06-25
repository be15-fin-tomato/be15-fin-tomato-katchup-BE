package be15fintomatokatchupbe.campaign.query.controller;

import be15fintomatokatchupbe.campaign.query.dto.response.*;
import be15fintomatokatchupbe.campaign.query.dto.request.PipelineSearchRequest;
import be15fintomatokatchupbe.campaign.query.service.CampaignQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name="캠페인")
@RestController
@AllArgsConstructor
@RequestMapping("/campaign")
@Tag(name = "CampaignQueryController", description = "캠페인 관련 조회 API")
public class CampaignQueryController {
    private final CampaignQueryService campaignQueryService;

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
}
