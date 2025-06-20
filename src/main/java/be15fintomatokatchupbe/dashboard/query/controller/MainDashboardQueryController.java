package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.dashboard.query.dto.response.*;
import be15fintomatokatchupbe.dashboard.query.service.MainDashboardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class MainDashboardQueryController {

    private final MainDashboardQueryService mainDashboardQueryService;

    @GetMapping("/sales-activity")
    public ResponseEntity<ApiResponse<SalesActivityResponse>> getSalesActivity(@AuthenticationPrincipal CustomUserDetail user) {
        Long userId = user.getUserId();
        SalesActivityResponse response = mainDashboardQueryService.getSalesActivity(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/client-company")
    public ResponseEntity<ApiResponse<List<ClientCompanyResponse>>> getClientCompany(@AuthenticationPrincipal CustomUserDetail user) {
        Long userId = user.getUserId();
        List<ClientCompanyResponse> response = mainDashboardQueryService.getClientCompany(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/schedule")
    public ResponseEntity<ApiResponse<List<TodayScheduleResponse>>> getTodaySchedule(@AuthenticationPrincipal CustomUserDetail user) {
        Long userId = user.getUserId();
        List<TodayScheduleResponse> response = mainDashboardQueryService.getTodaySchedule(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/list-up")
    public ResponseEntity<ApiResponse<List<ListupResponse>>> getListup(@AuthenticationPrincipal CustomUserDetail user) {
        Long userId = user.getUserId();
        List<ListupResponse> response = mainDashboardQueryService.getListupByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/proposal")
    public ResponseEntity<ApiResponse<List<ProposalResponse>>> getProposal(@AuthenticationPrincipal CustomUserDetail user) {
        Long userId = user.getUserId();
        List<ProposalResponse> response = mainDashboardQueryService.getProposalByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/contract")
    public ResponseEntity<ApiResponse<List<ContractResponse>>> getContract(@AuthenticationPrincipal CustomUserDetail user) {
        Long userId = user.getUserId();
        List<ContractResponse> response = mainDashboardQueryService.getContractByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
