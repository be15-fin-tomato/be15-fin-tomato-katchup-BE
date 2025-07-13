package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Instagram;
import be15fintomatokatchupbe.influencer.command.domain.repository.InstagramRepository;
import be15fintomatokatchupbe.infra.redis.InstagramTokenRepository;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import be15fintomatokatchupbe.oauth.query.dto.InstagramMediaStats;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramStatsResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstagramAccountQueryService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final InstagramTokenRepository instagramTokenRepository;
    private final InstagramRepository instagramRepository;

    @Value("${facebook.base-url}")
    private String baseUrl;

    public InstagramStatsResponse fetchStats(Long influencerId) {
        Instagram instagram = instagramRepository.findByInfluencerId(influencerId)
                .orElseThrow(() -> new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND));

        String igId = instagram.getAccountId();
        String token = instagramTokenRepository.getAccessToken(igId);
        if (token == null) {

            log.warn("[fetchStats] Redis에 토큰 없음. igId={}", igId);
            throw new BusinessException(OAuthErrorCode.TOKEN_NOT_FOUND);
        }

        try {
            Double dailyAvgViews = calculateAverage(token, igId, "views", 7);
            Double monthlyAvgViews = calculateAverage(token, igId, "views", 30);
            int totalFollowers = fetchFollowerCount(token, igId);
            Map<String, Double> followerRatioMap = fetchFollowTypeRatio(token, igId);
            Map<String, Double> genderDist = fetchDemographics(token, igId, "gender");
            Map<String, Double> ageDist = fetchDemographics(token, igId, "age");
            Map<String, Double> growthRates = fetchFollowerGrowthRate(token, igId);

            List<InstagramMediaStats> mediaStats = fetchTopMediaStats(token, igId);
            List<InstagramMediaStats> topPosts = mediaStats.stream()
                    .filter(m -> m.getMediaType().equals("IMAGE") || m.getMediaType().equals("CAROUSEL_ALBUM"))
                    .limit(3)
                    .collect(Collectors.toList());
            List<InstagramMediaStats> topVideos = mediaStats.stream()
                    .filter(m -> m.getMediaType().equals("VIDEO") || m.getMediaType().equals("REELS"))
                    .limit(3)
                    .collect(Collectors.toList());

            Double avgViews = calculateAverage(mediaStats, m -> Optional.ofNullable(m.getViewCount()).orElse(0));
            Double avgLikes = calculateAverage(mediaStats, m -> Optional.ofNullable(m.getLikeCount()).orElse(0));
            Double avgComments = calculateAverage(mediaStats, m -> Optional.ofNullable(m.getCommentCount()).orElse(0));
            int totalPosts = mediaStats.size();

            Double totalAccountReach = fetchAccountReach(token, igId, "days_28");

            return InstagramStatsResponse.builder()
                    .dailyAverageViews(dailyAvgViews)
                    .monthlyAverageViews(monthlyAvgViews)
                    .followerRatio(followerRatioMap.getOrDefault("FOLLOWER", 0.0))
                    .nonFollowerRatio(followerRatioMap.getOrDefault("NON_FOLLOWER", 0.0))
                    .followerAgeDistribution(ageDist)
                    .followerGenderDistribution(genderDist)
                    .dailyFollowerGrowthRate(growthRates.getOrDefault("daily", 0.0))
                    .weeklyFollowerGrowthRate(growthRates.getOrDefault("weekly", 0.0))
                    .monthlyFollowerGrowthRate(growthRates.getOrDefault("monthly", 0.0))
                    .topPosts(topPosts)
                    .topVideos(topVideos)
                    .totalFollowers(totalFollowers)
                    .reach(totalAccountReach)
                    .allMediaStats(mediaStats)
                    .averageViews(avgViews)
                    .averageLikes(avgLikes)
                    .averageComments(avgComments)
                    .totalPosts(totalPosts)
                    .genderFemale(genderDist.getOrDefault("female", 0.0))
                    .genderMale(genderDist.getOrDefault("male", 0.0))
                    .build();

        } catch (Exception e) {
            log.error("[fetchStats] Failed to fetch Instagram stats", e);
            throw new BusinessException(OAuthErrorCode.INSTAGRAM_STATS_ERROR);
        }
    }

    private Double calculateAverage(String token, String igId, String metric, int days) {
        LocalDate now = LocalDate.now();
        LocalDate since = now.minusDays(days);

        String url = String.format("%s/%s/insights?metric=%s&period=day&metric_type=total_value&since=%d&until=%d&access_token=%s",
                baseUrl, igId, metric,
                since.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                now.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                token);

        JsonNode data = fetchJson(url);
        JsonNode valuesNode = Optional.ofNullable(data.path("data"))
                .filter(JsonNode::isArray)
                .filter(d -> !d.isEmpty())
                .map(d -> d.get(0))
                .map(n -> n.path("values"))
                .orElse(null);

        if (valuesNode == null || !valuesNode.isArray()) {
            log.warn("No insight data returned for metric={}, igId={}", metric, igId);
            return 0.0;
        }

        int total = valuesNode.findValues("value").stream()
                .mapToInt(JsonNode::asInt)
                .sum();
        int count = valuesNode.size();

        return count > 0 ? total / (double) count : 0.0;
    }

    private int fetchFollowerCount(String token, String igId) {
        String url = String.format("%s/%s?fields=followers_count&access_token=%s",
                baseUrl, igId, token);

        JsonNode root = fetchJson(url);
        JsonNode countNode = root.path("followers_count");

        if (countNode.isMissingNode() || !countNode.isInt()) {
            log.warn("No valid followers_count found for igId={}", igId);
            return 0;
        }

        return countNode.asInt();
    }

    private Map<String, Double> fetchFollowTypeRatio(String token, String igId) {
        String url = String.format("%s/%s/insights?metric=views&period=day&metric_type=total_value&breakdown=follow_type&access_token=%s",
                baseUrl, igId, token);

        JsonNode results = fetchJson(url);
        JsonNode breakdowns = Optional.ofNullable(results.path("data"))
                .filter(JsonNode::isArray)
                .filter(d -> !d.isEmpty())
                .map(d -> d.get(0))
                .map(n -> n.path("total_value"))
                .map(n -> n.path("breakdowns"))
                .filter(JsonNode::isArray)
                .filter(b -> !b.isEmpty())
                .map(b -> b.get(0))
                .map(n -> n.path("results"))
                .orElse(null);

        if (breakdowns == null || !breakdowns.isArray()) {
            log.warn("No follow_type breakdowns found for igId={}", igId);
            return Map.of();
        }

        Map<String, Double> map = new HashMap<>();
        int total = 0;
        for (JsonNode r : breakdowns) {
            String type = r.path("dimension_values").get(0).asText();
            int value = r.path("value").asInt();
            total += value;
            map.put(type, (double) value);
        }
        for (String key : map.keySet()) {
            map.put(key, Math.round((map.get(key) / total) * 10000.0) / 100.0);
        }
        return map;
    }

    private Map<String, Double> fetchDemographics(String token, String igId, String breakdown) {
        String url = String.format(
                "%s/%s/insights?metric=follower_demographics&period=lifetime&metric_type=total_value&breakdown=%s&access_token=%s",
                baseUrl, igId, breakdown, token
        );

        JsonNode data = fetchJson(url);

        JsonNode valueNode = Optional.ofNullable(data.path("data"))
                .filter(JsonNode::isArray)
                .filter(d -> !d.isEmpty())
                .map(d -> d.get(0))
                .map(n -> n.path("values"))
                .filter(JsonNode::isArray)
                .filter(v -> !v.isEmpty())
                .map(v -> v.get(0).path("value"))
                .orElse(null);

        if (valueNode == null || !valueNode.isObject()) {
            log.warn("No follower demographic data returned for breakdown={}, igId={}", breakdown, igId);
            if (breakdown.equals("age")) {
                return Map.of("13-17", 0.0, "18-24", 0.0, "25-34", 0.0, "35-44", 0.0, "45-54", 0.0, "55-64", 0.0, "65+", 0.0);
            } else if (breakdown.equals("gender")) {
                return Map.of("male", 0.0, "female", 0.0);
            }
            return Map.of();
        }

        Map<String, Double> result = new LinkedHashMap<>();
        int total = 0;

        for (Iterator<Map.Entry<String, JsonNode>> it = valueNode.fields(); it.hasNext();) {
            Map.Entry<String, JsonNode> entry = it.next();
            int count = entry.getValue().asInt();
            result.put(entry.getKey(), (double) count);
            total += count;
        }

        for (String key : result.keySet()) {
            double percentage = Math.round((result.get(key) / total) * 10000.0) / 100.0;
            result.put(key, percentage);
        }

        return result;
    }

    private Map<String, Double> fetchFollowerGrowthRate(String token, String igId) {
        String url = String.format("%s/%s/insights?metric=follower_count&period=day&access_token=%s",
                baseUrl, igId, token);

        JsonNode valuesNode = Optional.ofNullable(fetchJson(url).path("data"))
                .filter(JsonNode::isArray)
                .filter(d -> !d.isEmpty())
                .map(d -> d.get(0))
                .map(n -> n.path("values"))
                .orElse(null);

        if (valuesNode == null || !valuesNode.isArray()) {
            log.warn("No follower_count data for igId={}", igId);
            return Map.of();
        }

        List<Integer> counts = new ArrayList<>();
        for (JsonNode v : valuesNode) counts.add(v.path("value").asInt());

        double daily = 0.0, weekly = 0.0, monthly = 0.0;
        if (counts.size() >= 2) {
            daily = ((double) (counts.get(counts.size() - 1) - counts.get(counts.size() - 2)) / counts.get(counts.size() - 2)) * 100;
        }
        if (counts.size() >= 8) {
            weekly = ((double) (counts.get(counts.size() - 1) - counts.get(counts.size() - 8)) / counts.get(counts.size() - 8)) * 100;
        }
        if (counts.size() >= 30) {
            monthly = ((double) (counts.get(counts.size() - 1) - counts.get(0)) / counts.get(0)) * 100;
        }
        return Map.of(
                "daily", Math.round(daily * 100.0) / 100.0,
                "weekly", Math.round(weekly * 100.0) / 100.0,
                "monthly", Math.round(monthly * 100.0) / 100.0
        );
    }

    private Double fetchAccountReach(String token, String igId, String period) {
        String url = String.format(
                "%s/%s/insights?metric=reach&period=%s&access_token=%s",
                baseUrl, igId, period, token
        );

        JsonNode data = fetchJson(url);
        JsonNode valuesNode = Optional.ofNullable(data.path("data"))
                .filter(JsonNode::isArray)
                .filter(d -> !d.isEmpty())
                .map(d -> d.get(0))
                .map(n -> n.path("values"))
                .filter(JsonNode::isArray)
                .filter(v -> !v.isEmpty())
                .map(v -> v.get(0).path("value"))
                .orElse(null);

        if (valuesNode == null || !valuesNode.isNumber()) {
            log.warn("No valid reach data returned for igId={}, period={}", igId, period);
            return 0.0;
        }

        return valuesNode.asDouble();
    }

    private List<InstagramMediaStats> fetchTopMediaStats(String token, String igId) {
        String url = String.format("%s/%s/media?fields=id,media_type,media_url,thumbnail_url&access_token=%s",
                baseUrl, igId, token);
        JsonNode mediaList = fetchJson(url).path("data");

        if (mediaList == null || !mediaList.isArray()) {
            log.warn("No media data for igId={}", igId);
            return List.of();
        }

        List<InstagramMediaStats> results = new ArrayList<>();
        for (JsonNode media : mediaList) {
            String id = media.path("id").asText();
            String type = media.path("media_type").asText();
            String mediaUrl = media.path("media_url").asText();
            String thumbnailUrl = media.path("thumbnail_url").asText();
            String insightUrl = String.format(
                    "%s/%s/insights?metric=reach,likes,comments,saved,shares&access_token=%s",
                    baseUrl, id, token
            );

            JsonNode insights = fetchJson(insightUrl).path("data");

            Map<String, Integer> metrics = new HashMap<>();
            if (insights.isArray()) {
                for (JsonNode m : insights) {
                    metrics.put(m.path("name").asText(), m.path("values").get(0).path("value").asInt());
                }
            }

            results.add(InstagramMediaStats.builder()
                    .mediaId(id)
                    .mediaType(type)
                    .mediaUrl(mediaUrl)
                    .thumbnailUrl(thumbnailUrl)
                    .impressions(0)
                    .reach(metrics.getOrDefault("reach", 0))
                    .viewCount(metrics.getOrDefault("reach", 0))
                    .likeCount(metrics.getOrDefault("likes", 0))
                    .commentCount(metrics.getOrDefault("comments", 0))
                    .saveCount(metrics.getOrDefault("saved", 0))
                    .shareCount(metrics.getOrDefault("shares", 0))
                    .engagement(metrics.getOrDefault("likes", 0)
                            + metrics.getOrDefault("comments", 0)
                            + metrics.getOrDefault("saved", 0)
                            + metrics.getOrDefault("shares", 0))
                    .build());

        }

        return results.stream()
                .sorted(Comparator.comparingInt(InstagramMediaStats::getEngagement).reversed())
                .collect(Collectors.toList());
    }

    private Double calculateAverage(List<InstagramMediaStats> mediaList, java.util.function.ToDoubleFunction<InstagramMediaStats> mapper) {
        return mediaList.stream()
                .mapToDouble(mapper)
                .average()
                .orElse(0.0);
    }

    private JsonNode fetchJson(String url) {
        try {
            String body = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode json = objectMapper.readTree(body);

            if (json.has("error")) {
                JsonNode errorNode = json.get("error");
                String msg = errorNode.path("message").asText();
                int code = errorNode.path("code").asInt();
                log.error("[fetchJson] API returned error: {} (code: {})", msg, code);
                throw new BusinessException(OAuthErrorCode.INSTAGRAM_STATS_ERROR);
            }

            return json;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[fetchJson] Failed URL: {}", url);
            log.error("[fetchJson] Error Message: {}", e.getMessage(), e);
            throw new BusinessException(OAuthErrorCode.INSTAGRAM_STATS_ERROR);
        }
    }

}