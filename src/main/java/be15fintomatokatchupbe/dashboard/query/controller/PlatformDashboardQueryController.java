package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignResponse;
import be15fintomatokatchupbe.dashboard.query.service.PlatformDashboardQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "대시보드")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class PlatformDashboardQueryController {
    private final PlatformDashboardQueryService platformDashboardQueryService;

    @Operation(summary = "인플루언서 별 캠페인 조회", description = "사용자는 플랫폼 대시보드에서 해당 인플루언서가 진행했던 캠페인의 목록을 조회할 수 있다.")
    @GetMapping("/campaign/{influencerId}")
    public ResponseEntity<ApiResponse<List<CampaignResponse>>> getSalesActivity(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable Long influencerId
    ) {
        List<CampaignResponse> response = platformDashboardQueryService.getCampaignList(influencerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
