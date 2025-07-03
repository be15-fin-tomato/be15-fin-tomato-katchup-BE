package be15fintomatokatchupbe.notification.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.notification.query.dto.response.NotificationsAllResponse;
import be15fintomatokatchupbe.notification.query.service.NotificationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationQueryController {

    private final NotificationQueryService notificationQueryService;

    @GetMapping("/all")
    @Operation(summary = "알림 전체 조회", description = "사용자는 본인의 모든 알림 목록을 조회할 수 있다.")
    public ResponseEntity<ApiResponse<NotificationsAllResponse>> getNotificationsAll(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        Long userId = customUserDetail.getUserId();
        NotificationsAllResponse response = notificationQueryService.getNotificationsAll(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}