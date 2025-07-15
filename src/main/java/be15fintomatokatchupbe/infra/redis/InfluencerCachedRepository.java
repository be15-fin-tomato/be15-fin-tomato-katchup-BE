package be15fintomatokatchupbe.infra.redis;

import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InfluencerCachedRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY = "influencer:list:initial";
    private static final String CACHE_AI_KEY = "influencer:ai:list:initial";
    private static final Duration TTL = Duration.ofMinutes(30);

    public InfluencerListResponse getInitialInfluencersFromCache() {
        try {
            return (InfluencerListResponse) redisTemplate.opsForValue().get(CACHE_KEY);
        } catch (Exception e) {
            log.warn("Redis 인플루언서 초기 목록 조회 실패: {}", e.getMessage());
            return null;
        }
    }

    public InfluencerListResponse getInitialAiInfluencersFromCache() {
        try {
            return (InfluencerListResponse) redisTemplate.opsForValue().get(CACHE_AI_KEY);
        } catch (Exception e) {
            log.warn("Redis AI 페이지 인플루언서 초기 목록 조회 실패: {}", e.getMessage());
            return null;
        }
    }

    public void setInitialInfluencersToCache(InfluencerListResponse response) {
        try {
            redisTemplate.opsForValue().set(CACHE_KEY, response, TTL);
            log.info("Redis 인플루언서 초기 목록 캐시 저장 완료");
        } catch (Exception e) {
            log.warn("Redis 인플루언서 초기 목록 캐시 저장 실패: {}", e.getMessage());
        }
    }

    public void setInitialAiInfluencersToCache(InfluencerListResponse response) {
        try {
            redisTemplate.opsForValue().set(CACHE_AI_KEY, response, TTL);
            log.info("Redis AI 페이지 인플루언서 초기 목록 캐시 저장 완료");
        } catch (Exception e) {
            log.warn("Redis AI 페이지 인플루언서 초기 목록 캐시 저장 실패: {}", e.getMessage());
        }
    }

    public void evictInitialInfluencers() {
        try {
            redisTemplate.delete(CACHE_KEY);
            log.info("Redis 인플루언서 초기 목록 캐시 삭제 완료");
        } catch (Exception e) {
            log.warn("Redis 인플루언서 초기 목록 캐시 삭제 실패: {}", e.getMessage());
        }
    }

    public void evictInitialAiInfluencers() {
        try {
            redisTemplate.delete(CACHE_AI_KEY);
            log.info("Redis AI 페이지 인플루언서 초기 목록 캐시 삭제 완료");
        } catch (Exception e) {
            log.warn("Redis AI 페이지 인플루언서 초기 목록 캐시 삭제 실패: {}", e.getMessage());
        }
    }
}
