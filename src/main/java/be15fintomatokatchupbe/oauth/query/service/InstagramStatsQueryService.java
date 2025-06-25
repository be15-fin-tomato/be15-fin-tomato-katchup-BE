package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.oauth.query.dto.InstagramMediaStats;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramStatsResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramStatsQueryService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL = "https://graph.facebook.com/v23.0";

    public InstagramStatsResponse fetchStats(String accessToken, String igAccountId) {
        LocalDate today = LocalDate.now();

        Double dailyAvgViews = calculateAverageViews(accessToken, igAccountId, today.minusDays(7), today);
        Double monthlyAvgViews = calculateAverageViews(accessToken, igAccountId, today.minusDays(30), today);

        Map<String, Integer> followTypeViews = fetchViewsByFollowType(accessToken, igAccountId, today.minusDays(7), today);
        int totalViews = followTypeViews.values().stream().mapToInt(i -> i).sum();
        Double followerRatio = totalViews == 0 ? 0.0 : followTypeViews.getOrDefault("followed", 0) * 100.0 / totalViews;
        Double nonFollowerRatio = totalViews == 0 ? 0.0 : followTypeViews.getOrDefault("unfollowed", 0) * 100.0 / totalViews;

        Map<String, Integer> ageDistribution = fetchDemographics(accessToken, igAccountId, "age");
        Map<String, Integer> genderDistribution = fetchDemographics(accessToken, igAccountId, "gender");

        List<Integer> followerCounts = fetchFollowerCounts(accessToken, igAccountId, today.minusDays(35), today);
        Double dailyGrowthRate = calculateGrowthRate(followerCounts, 1);
        Double weeklyGrowthRate = calculateGrowthRate(followerCounts, 7);
        Double monthlyGrowthRate = calculateGrowthRate(followerCounts, 30);

        List<InstagramMediaStats> topPosts = fetchTopMedia(accessToken, igAccountId, "IMAGE");
        List<InstagramMediaStats> topVideos = fetchTopMedia(accessToken, igAccountId, "VIDEO");

        return InstagramStatsResponse.builder()
                .dailyAverageViews(dailyAvgViews)
                .monthlyAverageViews(monthlyAvgViews)
                .followerRatio(followerRatio)
                .nonFollowerRatio(nonFollowerRatio)
                .followerAgeDistribution(ageDistribution)
                .followerGenderDistribution(genderDistribution)
                .dailyFollowerGrowthRate(dailyGrowthRate)
                .weeklyFollowerGrowthRate(weeklyGrowthRate)
                .monthlyFollowerGrowthRate(monthlyGrowthRate)
                .topPosts(topPosts)
                .topVideos(topVideos)
                .build();
    }

    private Double calculateAverageViews(String accessToken, String igAccountId, LocalDate since, LocalDate until) {
        String url = buildUrl(igAccountId, "/insights", Map.of(
                "metric", "views",
                "period", "day",
                "metric_type", "total_value",
                "since", toUnix(since),
                "until", toUnix(until),
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        int totalViews = extractSumValue(root);
        long days = until.toEpochDay() - since.toEpochDay();
        return days == 0 ? 0.0 : totalViews / (double) days;
    }

    private Map<String, Integer> fetchViewsByFollowType(String accessToken, String igAccountId, LocalDate since, LocalDate until) {
        String url = buildUrl(igAccountId, "/insights", Map.of(
                "metric", "views",
                "period", "day",
                "metric_type", "total_value",
                "breakdown", "follow_type",
                "since", toUnix(since),
                "until", toUnix(until),
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        return extractBreakdownValues(root, "follow_type");
    }

    private Map<String, Integer> fetchDemographics(String accessToken, String igAccountId, String type) {
        String url = buildUrl(igAccountId, "/insights", Map.of(
                "metric", "follower_demographics",
                "period", "lifetime",
                "timeframe", "this_month",
                "metric_type", "total_value",
                "breakdown", type,
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        return extractBreakdownValues(root, type);
    }

    private List<Integer> fetchFollowerCounts(String accessToken, String igAccountId, LocalDate since, LocalDate until) {
        String url = buildUrl(igAccountId, "/insights", Map.of(
                "metric", "follower_count",
                "period", "day",
                "since", toUnix(since),
                "until", toUnix(until),
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        return extractDailyValues(root);
    }

    private Double calculateGrowthRate(List<Integer> counts, int days) {
        if (counts.size() < days + 1) return 0.0;
        int end = counts.get(counts.size() - 1);
        int start = counts.get(counts.size() - 1 - days);
        return start == 0 ? 0.0 : (end - start) * 100.0 / start;
    }

    private List<InstagramMediaStats> fetchTopMedia(String accessToken, String igAccountId, String mediaType) {
        // TODO: 실제 media 목록 조회 및 insights API 호출 연동 필요
        return new ArrayList<>();
    }

    // ===== Utils =====

    private String buildUrl(String igAccountId, String path, Map<String, String> params) {
        StringBuilder url = new StringBuilder(BASE_URL + "/" + igAccountId + path + "?");
        params.forEach((k, v) -> url.append(k).append("=").append(v).append("&"));
        return url.toString();
    }

    private String toUnix(LocalDate date) {
        return String.valueOf(date.atStartOfDay().toEpochSecond(ZoneOffset.UTC));
    }

    private JsonNode getJson(String url) {
        try {
            String body = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
            return objectMapper.readTree(body);
        } catch (Exception e) {
            log.warn("Instagram API 호출 실패: {}", url, e);
            return objectMapper.createObjectNode();
        }
    }

    private int extractSumValue(JsonNode root) {
        try {
            JsonNode valueNode = root.path("data").findValue("total_value");
            return valueNode != null && valueNode.has("value") ? valueNode.get("value").asInt() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private Map<String, Integer> extractBreakdownValues(JsonNode root, String keyName) {
        Map<String, Integer> result = new HashMap<>();
        try {
            JsonNode breakdowns = root.path("data").get(0).path("total_value").path("breakdowns").get(0).path("results");
            for (JsonNode breakdown : breakdowns) {
                String key = breakdown.path("dimension_values").get(0).asText();
                int value = breakdown.path("value").asInt();
                result.put(key, value);
            }
        } catch (Exception e) {
            log.warn("Breakdown 추출 실패: {}", keyName, e);
        }
        return result;
    }

    private List<Integer> extractDailyValues(JsonNode root) {
        List<Integer> result = new ArrayList<>();
        try {
            JsonNode values = root.path("data").get(0).path("values");
            for (JsonNode val : values) {
                result.add(val.path("value").asInt());
            }
        } catch (Exception e) {
            log.warn("일별 값 추출 실패", e);
        }
        return result;
    }
}