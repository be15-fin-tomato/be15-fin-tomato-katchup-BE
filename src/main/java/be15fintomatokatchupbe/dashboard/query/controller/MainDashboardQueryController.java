package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.dashboard.query.dto.response.QuotationResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.*;
import be15fintomatokatchupbe.dashboard.query.service.MainDashboardQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "메인 대시보드")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class MainDashboardQueryController {

    private final MainDashboardQueryService mainDashboardQueryService;

    @Operation(summary = "신규 영업활동 조회", description = "사용자는 메인 대시보드에서 최근 30일 이내의 신규 영업활동 개수를 조회할 수 있다.")
    @GetMapping("/sales-activity")
    public ResponseEntity<ApiResponse<SalesActivityResponse>> getSalesActivity(
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        Long userId = user.getUserId();
        SalesActivityResponse response = mainDashboardQueryService.getSalesActivity(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "담당 고객사 조회", description = "사용자는 메인 대시보드에서 본인이 담당한 고객사의 목록을 조회할 수 있다.")
    @GetMapping("/client-company")
    public ResponseEntity<ApiResponse<List<ClientCompanyResponse>>> getClientCompany(
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        Long userId = user.getUserId();
        List<ClientCompanyResponse> response = mainDashboardQueryService.getClientCompany(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "오늘 일정 조회", description = "사용자는 메인 대시보드에서 오늘의 일정을 조회할 수 있다.")
    @GetMapping("/schedule")
    public ResponseEntity<ApiResponse<List<TodayScheduleResponse>>> getTodaySchedule(
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        Long userId = user.getUserId();
        List<TodayScheduleResponse> response = mainDashboardQueryService.getTodaySchedule(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "담당 리스트업 조회", description = "사용자는 메인 대시보드에서 자신이 담당한 리스트업의 목록을 조회할 수 있다.")
    @GetMapping("/list-up")
    public ResponseEntity<ApiResponse<List<ListupResponse>>> getListup(
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        Long userId = user.getUserId();
        List<ListupResponse> response = mainDashboardQueryService.getListupByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "담당 제안 조회", description = "사용자는 메인 대시보드에서 자신이 담당한 제안의 목록을 조회할 수 있다.")
    @GetMapping("/proposal")
    public ResponseEntity<ApiResponse<List<ProposalResponse>>> getProposal(
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        Long userId = user.getUserId();
        List<ProposalResponse> response = mainDashboardQueryService.getProposalByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "담당 계약 조회", description = "사용자는 메인 대시보드에서 자신이 담당한 계약의 목록을 조회할 수 있다.")
    @GetMapping("/contract")
    public ResponseEntity<ApiResponse<List<ContractResponse>>> getContract(
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        Long userId = user.getUserId();
        List<ContractResponse> response = mainDashboardQueryService.getContractByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "담당 견적 조회", description = "사용자는 메인 대시보드에서 자신이 담당한 견적의 목록을 조회할 수 있다.")
    @GetMapping("/quotation")
    public ResponseEntity<ApiResponse<List<QuotationResponse>>> getQuotation(
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        Long userId = user.getUserId();
        List<QuotationResponse> response = mainDashboardQueryService.getQuotationByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
