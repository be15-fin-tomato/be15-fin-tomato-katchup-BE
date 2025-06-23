package be15fintomatokatchupbe.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class YoutubeTokenRepository {
    private final String YOUTUBE_TOKEN_PREFIX = "youtube:connect:";
    private final String YOUTUBE_ACCESS_TOKEN = "youtube:access:";
    private final Duration TTL = Duration.ofMinutes(10);

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String channelId, String refreshToken){
        redisTemplate.opsForValue().set(YOUTUBE_TOKEN_PREFIX + channelId, refreshToken, TTL);
    }

    public void saveAccessToken(String channelId, String accessToken, Duration ttl){
        redisTemplate.opsForValue().set(YOUTUBE_ACCESS_TOKEN + channelId, accessToken, ttl);
    }

    public String find(String channelId){
        return (String) redisTemplate.opsForValue().get(YOUTUBE_TOKEN_PREFIX + channelId);
    }

    public String findAccessToken(String channelId){
        return (String) redisTemplate.opsForValue().get(YOUTUBE_ACCESS_TOKEN + channelId);
    }

    public void delete(String channelId) {
        redisTemplate.delete(YOUTUBE_TOKEN_PREFIX + channelId);
    }

    public void deleteAccessToken(String channelId) {
        redisTemplate.delete(YOUTUBE_ACCESS_TOKEN + channelId);
    }
}


