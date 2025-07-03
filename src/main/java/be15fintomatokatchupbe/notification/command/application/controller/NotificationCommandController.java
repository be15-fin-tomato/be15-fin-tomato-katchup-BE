package be15fintomatokatchupbe.notification.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.notification.command.application.service.NotificationCommandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationCommandController {

    private final NotificationCommandService notificationCommandService;

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 삭제", description = "사용자는 본인의 알림을 삭제할 수 있다.")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable Long notificationId
    ){
        Long userId = customUserDetail.getUserId();
        notificationCommandService.deleteNotification(userId, notificationId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

//    @DeleteMapping("/all")
//    @Operation(summary = "알림 전체 삭제", description = "사용자는 본인의 알림을 모두 삭제할 수 있다.")
//    public ResponseEntity<ApiResponse<Void>> deleteAllNotification(
//            @AuthenticationPrincipal CustomUserDetail customUserDetail
//    ){
//        Long userId = customUserDetail.getUserId();
//        notificationCommandService.deleteAllNotification(userId);
//        return ResponseEntity.ok(ApiResponse.success(null));
//    }
}