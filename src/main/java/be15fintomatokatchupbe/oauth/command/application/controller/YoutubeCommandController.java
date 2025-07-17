package be15fintomatokatchupbe.oauth.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.oauth.command.application.Service.YoutubeCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "인플루언서-유튜브 계정 연동")
@Slf4j
@RestController
@RequestMapping("/oauth2/youtube")
@RequiredArgsConstructor
public class YoutubeCommandController {

    private final YoutubeCommandService youtubeCommandService;

    @Operation(summary = "OAuth 콜백: code로 유튜브 토큰 발급 및 프론트 리디렉션", description = "사용자는 인가 코드를 받아 유튜브 액세스 토큰을 발급 받을 수 있다.")
    @GetMapping("/callback")
    public ResponseEntity<Void> registerYoutube(
            @RequestParam("code") String code,
            @RequestParam("state") Long influencerId
    ) {
//        String frontendRedirectBaseUrl = "http://localhost:5173/oauth2/youtube/callback";
        String frontendRedirectBaseUrl = "https://tomato-katchup.xyz/oauth2/youtube/callback";
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

    @Operation(summary = "유튜브 연동 해제", description = "사용자는 해당하는 인플루언서와 유튜브 채널의 연동을 해제할 수 있다.")
    @DeleteMapping("/{influencerId}/disconnect")
    public ResponseEntity<ApiResponse<Void>> disconnectYoutube(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long influencerId
    ) {
        youtubeCommandService.disconnectYoutubeAccount(influencerId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
