package be15fintomatokatchupbe.oauth.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.oauth.command.application.Service.YoutubeCommandService;
import be15fintomatokatchupbe.oauth.command.application.scheduler.YoutubeStatsSnapshotScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/oauth2/youtube")
@RequiredArgsConstructor
public class YoutubeCommandController {

    private final YoutubeCommandService youtubeCommandService;
    private final YoutubeStatsSnapshotScheduler scheduler;

    @GetMapping("/callback")
    public ResponseEntity<Void> registerYoutube(
            @RequestParam("code") String code,
            @RequestParam("state") Long influencerId
    ) {
        String frontendRedirectBaseUrl = "http://localhost:5173/oauth2/youtube/callback";
        String redirectUrl;

        try {
            youtubeCommandService.registerYoutubeByOAuth(code, influencerId);

            redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectBaseUrl)
                    .queryParam("status", "success")
                    .build()
                    .toUriString();
        } catch (BusinessException e) {
            String encodedErrorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectBaseUrl)
                    .queryParam("status", "fail")
                    .queryParam("error_message", encodedErrorMessage)
                    .build()
                    .toUriString();
            log.error("유튜브 계정 연동 실패 (BusinessException): {}", e.getMessage());
        } catch (Exception e) {
            String encodedErrorMessage = URLEncoder.encode("내부 서버 오류가 발생했습니다.", StandardCharsets.UTF_8);
            redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectBaseUrl)
                    .queryParam("status", "fail")
                    .queryParam("error_message", encodedErrorMessage)
                    .build()
                    .toUriString();
            log.error("유튜브 계정 연동 실패 (알 수 없는 오류): {}", e.getMessage(), e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

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
