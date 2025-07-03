package be15fintomatokatchupbe.notification.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.notification.command.application.dto.request.FcmTokenRequest;
import be15fintomatokatchupbe.notification.command.application.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "fcm 알림")
@RestController
@RequestMapping("/fcm/token")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping
    @Operation(summary = "FCM 토큰 등록", description = "로그인한 사용자의 FCM 토큰을 서버에 등록합니다.")
    public ResponseEntity<ApiResponse<Void>> getFcmToken(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody FcmTokenRequest fcmTokenRequest
    ) {

        Long userId = userDetail.getUserId();
        fcmService.getFcmToken(userId, fcmTokenRequest);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
