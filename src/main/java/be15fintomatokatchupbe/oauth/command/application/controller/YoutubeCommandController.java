package be15fintomatokatchupbe.oauth.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.oauth.command.application.Service.YoutubeCommandService;
import be15fintomatokatchupbe.oauth.command.application.scheduler.YoutubeStatsSnapshotScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2/youtube")
@RequiredArgsConstructor
public class YoutubeCommandController {

    private final YoutubeCommandService youtubeCommandService;
    private final YoutubeStatsSnapshotScheduler scheduler;

    @DeleteMapping("/{influencerId}/disconnect")
    public ResponseEntity<ApiResponse<Void>> disconnectYoutube(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long influencerId
    ) {
        youtubeCommandService.disconnectYoutubeAccount(influencerId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 스케줄러 테스트용
    @PostMapping("/stats-scheduler")
    public ResponseEntity<Void> testYoutubeScheduler(
            @AuthenticationPrincipal CustomUserDetail userDetail
            ) {
        scheduler.collectDailyYoutubeStats();
        return ResponseEntity.ok().build();
    }
}
