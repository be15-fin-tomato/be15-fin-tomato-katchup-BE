package be15fintomatokatchupbe.notification.command.application.controller;

import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.notification.command.domain.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class NotificationSseController {

    private final SseEmitterRepository  sseEmitterRepository;

    @GetMapping(value = "/subscribe",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    public SseEmitter subscribe(
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        Long userId = userDetail.getUserId();
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> sseEmitterRepository.delete(userId));
        emitter.onTimeout(() -> sseEmitterRepository.delete(userId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            sseEmitterRepository.delete(userId);
        }
        return emitter;
    }
}
