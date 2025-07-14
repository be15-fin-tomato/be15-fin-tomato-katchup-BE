package be15fintomatokatchupbe.oauth.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.oauth.command.application.Service.InstagramCommandService;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramTokenResponse;
import be15fintomatokatchupbe.oauth.query.service.InstagramTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인플루언서-인스타그램 계정 연동")
@RestController
@RequestMapping("/oauth2/instagram")
@RequiredArgsConstructor
public class InstagramCommandController {
    private final InstagramTokenService instagramTokenService;
    private final InstagramCommandService instagramCommandService;

    @Operation(summary = "OAuth 콜백: code로 인스타그램 토큰 발급 및 프론트 리디렉션", description = "사용자는 인가 코드를 받아 인스타그램 액세스 토큰을 발급 받을 수 있다.")
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<InstagramTokenResponse>> handleInstagramCallback(
            @RequestParam("code") String code,
            @RequestParam("state") Long influencerId
    ) {
        InstagramTokenResponse tokenResponse = instagramTokenService.exchangeCodeForToken(code, influencerId);
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @Operation(summary = "인스타그램 연동 해제", description = "사용자는 해당하는 인플루언서와 인스타그램 계정의 연동을 해제할 수 있다.")
    @DeleteMapping("/{influencerId}/disconnect")
    public ResponseEntity<ApiResponse<Void>> disconnectYoutube(
            @PathVariable Long influencerId
    ) {
        instagramCommandService.disconnectYoutubeAccount(influencerId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
