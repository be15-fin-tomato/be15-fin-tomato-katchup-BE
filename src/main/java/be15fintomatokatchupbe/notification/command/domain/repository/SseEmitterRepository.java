package be15fintomatokatchupbe.notification.command.domain.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId,  SseEmitter emitter) {
        emitters.put(userId, emitter);
        return emitter;
    }

    public Optional<SseEmitter> get(Long userId) {
        return Optional.ofNullable(emitters.get(userId));
    }

    public void delete(Long userId) {
        emitters.remove(userId);
    }

}
