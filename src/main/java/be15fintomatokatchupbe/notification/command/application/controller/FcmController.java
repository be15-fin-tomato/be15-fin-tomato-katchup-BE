package be15fintomatokatchupbe.notification.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.notification.command.application.dto.request.FcmTokenRequest;
import be15fintomatokatchupbe.notification.command.application.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fcm/token")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> getFcmToken(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody FcmTokenRequest fcmTokenRequest
    ) {

        Long userId = userDetail.getUserId();
        fcmService.getFcmToken(userId, fcmTokenRequest);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
