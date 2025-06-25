package be15fintomatokatchupbe.oauth.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramStatsResponse;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramTokenResponse;
import be15fintomatokatchupbe.oauth.query.service.InstagramStatsQueryService;
import be15fintomatokatchupbe.oauth.query.service.InstagramTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Instagram OAuth", description = "인스타그램 OAuth 인증 및 통계 API")
@RestController
@RequestMapping("/oauth2/instagram")
@RequiredArgsConstructor
@Slf4j
public class InstagramQueryController {

    private final InstagramStatsQueryService instagramStatsQueryService;
    private final InstagramTokenService instagramTokenService;

    // TODO: 프론트에서 처리해야됨 (지금은 백엔드 테스트용)
    @Operation(summary = "OAuth 콜백: code로 토큰 발급 및 프론트 리디렉션", description = "사용자는 인가 코드를 받아 액세스 토큰을 발급 받을 수 있다.")
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<InstagramTokenResponse>> handleInstagramCallback(
            @RequestParam("code") String code
    ) {
        InstagramTokenResponse tokenResponse = instagramTokenService.exchangeCodeForToken(code);
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @Operation(summary = "Authorization code로 access token 및 Instagram 계정 ID 발급", description = "사용자는 액세스 토큰을 발급 받아 통계 정보를 조회할 수 있다. ")
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<InstagramTokenResponse>> exchangeCodeForToken(
            @RequestBody Map<String, String> body
    ) {
        String code = body.get("code");
        InstagramTokenResponse response = instagramTokenService.exchangeCodeForToken(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "인스타그램 통계 조회", description = "사용자는 인플루언서 인스타그램의 통계 자료를 조회할 수 있다. ")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<InstagramStatsResponse>> getInstagramStats(
            @Parameter(description = "Instagram access token") @RequestParam("accessToken") String accessToken,
            @Parameter(description = "Instagram 비즈니스 계정 ID") @RequestParam("igAccountId") String igAccountId
    ) {
        log.info("[GET] /oauth2/instagram/stats - accessToken=***{}, igAccountId={} ", accessToken.length(), igAccountId);
        InstagramStatsResponse response = instagramStatsQueryService.fetchStats(accessToken, igAccountId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
