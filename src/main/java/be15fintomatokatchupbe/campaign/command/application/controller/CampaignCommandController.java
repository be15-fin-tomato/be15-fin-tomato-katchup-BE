package be15fintomatokatchupbe.campaign.command.application.controller;

import be15fintomatokatchupbe.campaign.command.application.dto.request.*;
import be15fintomatokatchupbe.campaign.command.application.service.*;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "캠페인")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/campaign")
public class CampaignCommandController {

    private final CampaignCommandService campaignCommandService;
    private final ProposalCommandService proposalCommandService;
    private final ContractCommandService contractCommandService;
    private final QuotationCommandService quotationCommandService;
    private final RevenueCommandService revenueCommandService;


    @PostMapping("/chance/create")
    public ResponseEntity<ApiResponse<Void>> createChance(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody CreateChanceRequest request
    ){
        Long userId =user.getUserId();

        campaignCommandService.createChance(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));

    }

    @PostMapping("/proposal/create")
    public ResponseEntity<ApiResponse<Void>> createProposal(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody CreateProposalRequest request
            ){
        Long userId = user.getUserId();

//        campaignCommandService.createProposal(userId, request);
        proposalCommandService.createProposal(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/quotation/create")
    public ResponseEntity<ApiResponse<Void>> createQuotation(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody CreateQuotationRequest request
    ) {
        Long userId = user.getUserId();

        quotationCommandService.createQuotation(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/contract/create")
    public ResponseEntity<ApiResponse<Void>> createContract(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestPart("request") CreateContractRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ){
        Long userId = userDetail.getUserId();

        contractCommandService.createContract(userId, request, files);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/revenue/create")
    public ResponseEntity<ApiResponse<Void>> createRevenue(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestPart("request")CreateRevenueRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ){
        Long userId = userDetail.getUserId();

        revenueCommandService.createRevenue(userId, request, files);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 캠페인 상세 수정
    @PutMapping("/chance/update")
    public ResponseEntity<ApiResponse<Void>> updateChance(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody UpdateChanceRequest request
    ) {
        log.info("[Controller] 캠페인 수정 요청 들어옴. campaignId = {}", request.getCampaignId());

        campaignCommandService.updateChance(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 캠페인 상세 삭제
    @DeleteMapping("/{campaignId}")
    public ResponseEntity<ApiResponse<Void>> deleteCampaign(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable Long campaignId
    ) {
        log.info("[Controller] 캠페인 삭제 요청. campaignId = {}", campaignId);
        campaignCommandService.deleteCampaign(campaignId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    @PutMapping("/quotation")
    public ResponseEntity<ApiResponse<Void>> updateQuotation(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody UpdateQuotationRequest request
    ){
        Long userId = user.getUserId();

        quotationCommandService.updateQuotation(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/contract")
    public ResponseEntity<ApiResponse<Void>> updateContract(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestPart("request") UpdateContractRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ){
        Long userId = user.getUserId();
        contractCommandService.updateContract(userId, request, files);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/revenue")
    public ResponseEntity<ApiResponse<Void>> updateRevenue(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestPart("request") UpdateRevenueRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ){
        Long userId = user.getUserId();
        revenueCommandService.updateRevenue(userId, request, files);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/proposal/{pipelineId}")
    public ResponseEntity<ApiResponse<Void>> deleteProposal(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestParam("pipelineId") Long pipelineId
    ) {
        Long userId = user.getUserId();

        proposalCommandService.deleteProposal(userId, pipelineId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

}