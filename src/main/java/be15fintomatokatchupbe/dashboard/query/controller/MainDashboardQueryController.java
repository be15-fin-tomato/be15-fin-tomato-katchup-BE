package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.dashboard.query.dto.response.SalesActivityResponse;
import be15fintomatokatchupbe.dashboard.query.service.MainDashboardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
