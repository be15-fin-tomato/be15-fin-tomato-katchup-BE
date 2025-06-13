package be15fintomatokatchupbe.infra.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;


@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRepository {
    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(Long userId, String refreshToken) {
        Duration ttl = Duration.ofMillis(jwtRefreshExpiration);

        redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, ttl);
    }

    public String find(Long userId) {
        return (String) redisTemplate.opsForValue().get("refresh:" + userId);
    }

    public void delete(Long userId) {
        redisTemplate.delete("refresh:" + userId);
    }
}
