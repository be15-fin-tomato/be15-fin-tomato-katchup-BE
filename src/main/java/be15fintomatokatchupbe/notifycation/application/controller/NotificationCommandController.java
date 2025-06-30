package be15fintomatokatchupbe.notifycation.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NotificationCommandController {

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createNotification(@RequestBody NotificationCreateRequest request) {

    }
}
