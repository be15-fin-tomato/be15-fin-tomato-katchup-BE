package be15fintomatokatchupbe.auth.query.controller;


import be15fintomatokatchupbe.auth.command.dto.response.TokenResponse;
import be15fintomatokatchupbe.auth.query.dto.request.LoginRequest;
import be15fintomatokatchupbe.auth.query.service.AuthQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.user.annotation.ValidEmail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Tag(name = "회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthQueryController {
    private final AuthQueryService authQueryService;

    /* 로그인 */
    @Operation(summary = "로그인",description = "사용자는 로그인을 할 수 있다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestBody @Valid LoginRequest request
    ){
        TokenResponse response = authQueryService.login(request);

        return buildTokenResponse(response);
    }

    /* 재발급 */
    @Operation(summary = "토큰 재발급",description = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ){
        TokenResponse tokenResponse = authQueryService.reissue(refreshToken);

        return buildTokenResponse(tokenResponse);
    }

    /* 비밀번호 찾기 */
    @Operation(summary = "비밀번호 찾기",description = "사용자는 비밀번호를 찾을 수 있다.")
    @GetMapping("/find/password")
    public ResponseEntity<ApiResponse<Void>> findPassword(
            @RequestParam String loginId,
            @RequestParam @ValidEmail String email
    ){
        authQueryService.findPassword(loginId, email);

        return ResponseEntity.ok(ApiResponse.success(null));
    }


    /* accessToken -> body
     * refreshToken -> cookie*/
    private ResponseEntity<ApiResponse<TokenResponse>> buildTokenResponse(TokenResponse tokenResponse){
        ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(tokenResponse));
    }

    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)                     // HttpOnly 속성 설정 (JavaScript 에서 접근 불가)
                // .secure(true)                    // HTTPS 환경일 때만 전송 (운영 환경에서 활성화 권장)
                .path("/")                          // 쿠키 범위 : 전체 경로
                .maxAge(Duration.ofDays(7))         // 쿠키 만료 기간 : 7일
                .sameSite("Strict")                 // CSRF 공격 방어를 위한 SameSite 설정
                .build();
    }
}
