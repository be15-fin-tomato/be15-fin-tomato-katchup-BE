package be15fintomatokatchupbe.auth.command.controller;

import be15fintomatokatchupbe.auth.command.service.AuthCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthCommandController {

    private final AuthCommandService authCommandService;

    /* 로그아웃 */
    @Operation(summary = "로그아웃",description = "사용자는 로그아웃을 할 수 있다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ){
        if(refreshToken != null){
            authCommandService.logout(userDetail, refreshToken);
        }

        ResponseCookie deleteCookie =createDeleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success(null));
    }

    /* 쿠키 삭제용*/
    private ResponseCookie createDeleteRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)     // HttpOnly 유지
//                 .secure(true)    // HTTPS 환경에서만 사용 시 주석 해제
                .path("/")          // 동일 path 범위
                .maxAge(0)          // 즉시 만료
                .sameSite("Strict") // SameSite 유지
                .build();
    }
}
