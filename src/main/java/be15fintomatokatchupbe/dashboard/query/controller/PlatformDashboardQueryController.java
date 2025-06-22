package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignResponse;
import be15fintomatokatchupbe.dashboard.query.service.PlatformDashboardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class PlatformDashboardQueryController {
    private final PlatformDashboardQueryService platformDashboardQueryService;

    @GetMapping("/campaign/{influencerId}")
    public ResponseEntity<ApiResponse<List<CampaignResponse>>> getSalesActivity(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable Long influencerId
    ) {
        List<CampaignResponse> response = platformDashboardQueryService.getCampaignList(influencerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
