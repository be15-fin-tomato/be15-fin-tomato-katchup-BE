package be15fintomatokatchupbe.notifycation.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NotificationQueryController {

//    @GetMapping("")
//    public ResponseEntity<ApiResponse<Void>> getNotification() {
//        return null;
//    }

//    @GetMapping("/subscribe")
//    public SseEmitter subscribe() {
//
//    }
}
