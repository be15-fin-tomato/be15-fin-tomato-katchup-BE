package be15fintomatokatchupbe.campaign.command.application.controller;

import be15fintomatokatchupbe.campaign.command.application.dto.request.*;
import be15fintomatokatchupbe.campaign.command.application.service.*;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.user.exception.UserErrorCode;
import io.swagger.v3.oas.annotations.Operation;
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
    private final ProposalCommandService proposalCommandService;
    private final ContractCommandService contractCommandService;
    private final ListupCommandService listupCommandService;
    private final QuotationCommandService quotationCommandService;
    private final RevenueCommandService revenueCommandService;
    private final IdeaCommandService ideaCommandService;
    private final UserRepository userRepository;
    private final UserHelperService userHelperService;


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

    // 견적 의견 삭제 기능
    @Operation(summary = "견적 의견 삭제", description = "사용자는 견적에 작성된 의견을 삭제할 수 있다.(soft delete)")
    @DeleteMapping("quotation/idea/{ideaId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuotationIdea(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable Long ideaId
    ){
        User user = userHelperService.findValidUser(customUserDetail.getUserId());
        ideaCommandService.deleteQuotationIdea(user, ideaId);
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
}