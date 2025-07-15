package be15fintomatokatchupbe.infra.redis;

import be15fintomatokatchupbe.campaign.query.dto.response.CampaignAiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AIRecommendCampaignRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "campaigns:ai:initial";
    private static final Duration TTL = Duration.ofMinutes(30);

    public CampaignAiResponse getInitialCampaignsFromCache() {
        try {
            return (CampaignAiResponse) redisTemplate.opsForValue().get(CACHE_KEY);
        } catch (Exception e) {
            return null;
        }
    }

    public void setInitialCampaignsToCache(CampaignAiResponse response) {
        try {
            redisTemplate.opsForValue().set(CACHE_KEY, response, TTL);
        } catch (Exception e) {
            log.warn("Redis 캐시 저장 실패: {}", e.getMessage());
        }
    }

    public void evictInitialCampaigns() {
        try {
            redisTemplate.delete(CACHE_KEY);
        } catch (Exception e) {
            log.warn("Redis 캐시 삭제 실패: {}", e.getMessage());
        }
    }
}
