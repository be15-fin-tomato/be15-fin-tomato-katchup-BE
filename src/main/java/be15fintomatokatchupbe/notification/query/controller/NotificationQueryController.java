package be15fintomatokatchupbe.notification.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationQueryController {

    @GetMapping("")
    public ResponseEntity<ApiResponse<Void>> getNotification() {
        return null;
    }

}
