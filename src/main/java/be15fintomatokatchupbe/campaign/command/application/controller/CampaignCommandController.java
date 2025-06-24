package be15fintomatokatchupbe.campaign.command.application.controller;

import be15fintomatokatchupbe.campaign.command.application.dto.request.*;
import be15fintomatokatchupbe.campaign.command.application.service.CampaignCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        campaignCommandService.createProposal(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/quotation/create")
    public ResponseEntity<ApiResponse<Void>> createQuotation(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody CreateQuotationRequest request
    ) {
        Long userId = user.getUserId();

        campaignCommandService.createQuotation(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/contract/create")
    public ResponseEntity<ApiResponse<Void>> createContract(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestPart("request") CreateContractRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ){
        Long userId = userDetail.getUserId();

        campaignCommandService.createContract(userId, request, files);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/revenue/create")
    public ResponseEntity<ApiResponse<Void>> createRevenue(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestPart("request")CreateRevenueRequest request,
            @RequestPart(required = false) List<MultipartFile> files
    ){
        Long userId = userDetail.getUserId();

        campaignCommandService.createRevenue(userId, request, files);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

}