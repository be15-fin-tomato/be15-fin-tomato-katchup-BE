package be15fintomatokatchupbe.notification.command.application.controller;

import be15fintomatokatchupbe.notification.command.domain.repository.SseEmitterRepository;
import be15fintomatokatchupbe.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
@Slf4j
public class NotificationSseController {

    private final SseEmitterRepository sseEmitterRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader("Authorization") String authHeader) {
        // 토큰 파싱
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);

        // 토큰 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        Long userId = jwtTokenProvider.getUserIdFromJWT(token);
        log.info("SSE 구독 요청 - userId: {}", userId);

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> sseEmitterRepository.delete(userId));
        emitter.onTimeout(() -> sseEmitterRepository.delete(userId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            log.error("SSE 초기 연결 실패", e);
            sseEmitterRepository.delete(userId);
        }

        return emitter;
    }
}
