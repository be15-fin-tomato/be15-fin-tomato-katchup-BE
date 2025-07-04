package be15fintomatokatchupbe.infra.redis;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstagramTokenRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "instagram:token:";

    /* 액세스 토큰 저장 (기본 1시간 유효) */
    public void saveAccessToken(String userId, String accessToken, long ttlSeconds) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, accessToken, ttlSeconds, TimeUnit.SECONDS);
    }

    /*  액세스 토큰 조회 */
    public String getAccessToken(String igAccountId) {
        String key = "instagram:token:" + igAccountId;
        Object tokenObj = redisTemplate.opsForValue().get(key);
        log.debug("[getAccessToken] key={}, exists={}", key, tokenObj != null);
        if (tokenObj == null) {
            throw new BusinessException(OAuthErrorCode.TOKEN_NOT_FOUND);
        }
        return tokenObj.toString();
    }

    public void delete(String userId) {
        redisTemplate.delete(TOKEN_PREFIX + userId);
    }

    public void deleteAccessToken(String userId) {
        redisTemplate.delete(TOKEN_PREFIX + userId);
    }

}
