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
import java.util.stream.Collectors;

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

        Map<String, Integer> followTypeViews = fetchBreakdownData(accessToken, igAccountId, "views", "follow_type", today.minusDays(7), today);
        int totalViews = followTypeViews.values().stream().mapToInt(Integer::intValue).sum();
        Double followerRatio = totalViews == 0 ? 0.0 : followTypeViews.getOrDefault("followed", 0) * 100.0 / totalViews;
        Double nonFollowerRatio = totalViews == 0 ? 0.0 : followTypeViews.getOrDefault("unfollowed", 0) * 100.0 / totalViews;

        Map<String, Integer> ageDistribution = fetchDemographicData(accessToken, igAccountId, "age");
        Map<String, Integer> genderDistribution = fetchDemographicData(accessToken, igAccountId, "gender");

        List<Integer> followerCounts = fetchTimeSeriesData(accessToken, igAccountId, "follower_count", today.minusDays(35), today);
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

    private Double calculateAverageViews(String token, String igId, LocalDate from, LocalDate to) {
        List<Integer> views = fetchTimeSeriesData(token, igId, "views", from, to);
        return views.isEmpty() ? 0.0 : views.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    private Map<String, Integer> fetchBreakdownData(String token, String igId, String metric, String breakdown, LocalDate from, LocalDate to) {
        String url = buildUrl(igId, "/insights", Map.of(
                "metric", metric,
                "period", "day",
                "metric_type", "total_value",
                "breakdown", breakdown,
                "since", toUnix(from),
                "until", toUnix(to),
                "access_token", token
        ));

        JsonNode root = getJson(url);
        return extractBreakdownValues(root, breakdown);
    }

    private Map<String, Integer> fetchDemographicData(String token, String igId, String type) {
        String url = buildUrl(igId, "/insights", Map.of(
                "metric", "follower_demographics",
                "period", "lifetime",
                "timeframe", "this_month",
                "metric_type", "total_value",
                "breakdown", type,
                "access_token", token
        ));

        JsonNode root = getJson(url);
        return extractBreakdownValues(root, type);
    }

    private List<Integer> fetchTimeSeriesData(String token, String igId, String metric, LocalDate from, LocalDate to) {
        String url = buildUrl(igId, "/insights", Map.of(
                "metric", metric,
                "period", "day",
                "since", toUnix(from),
                "until", toUnix(to),
                "access_token", token
        ));

        JsonNode root = getJson(url);
        List<Integer> values = new ArrayList<>();
        try {
            for (JsonNode val : root.path("data").get(0).path("values")) {
                values.add(val.path("value").asInt());
            }
        } catch (Exception e) {
            log.warn("Time series 추출 실패: {}", metric, e);
        }
        return values;
    }

    private Double calculateGrowthRate(List<Integer> counts, int days) {
        if (counts.size() < days + 1) return 0.0;
        int start = counts.get(counts.size() - 1 - days);
        int end = counts.get(counts.size() - 1);
        return start == 0 ? 0.0 : (end - start) * 100.0 / start;
    }

    private List<InstagramMediaStats> fetchTopMedia(String token, String igId, String type) {
        List<InstagramMediaStats> mediaList = new ArrayList<>();
        String mediaUrl = buildUrl(igId, "/media", Map.of(
                "fields", "id,caption,media_type",
                "access_token", token
        ));

        JsonNode mediaNodes = getJson(mediaUrl);
        for (JsonNode media : mediaNodes.path("data")) {
            if (!media.path("media_type").asText().equalsIgnoreCase(type)) continue;

            String id = media.path("id").asText();
            String caption = media.path("caption").asText("");
            String detailUrl = BASE_URL + "/" + id + "/insights?metric=impressions,reach,engagement&access_token=" + token;

            try {
                JsonNode insights = getJson(detailUrl);
                int impressions = insights.path("data").get(0).path("values").get(0).path("value").asInt();
                int reach = insights.path("data").get(1).path("values").get(0).path("value").asInt();
                int engagement = insights.path("data").get(2).path("values").get(0).path("value").asInt();

                mediaList.add(InstagramMediaStats.builder()
                        .mediaId(id)
                        .caption(caption)
                        .mediaType(type)
                        .impressions(impressions)
                        .reach(reach)
                        .engagement(engagement)
                        .build());
            } catch (Exception e) {
                log.warn("미디어 통계 조회 실패: {}", id, e);
            }
        }

        return mediaList.stream()
                .sorted(Comparator.comparingInt(InstagramMediaStats::getEngagement).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private String buildUrl(String igId, String path, Map<String, String> params) {
        StringBuilder url = new StringBuilder(BASE_URL + "/" + igId + path + "?");
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

    private Map<String, Integer> extractBreakdownValues(JsonNode root, String keyName) {
        Map<String, Integer> result = new HashMap<>();
        try {
            JsonNode results = root.path("data").get(0).path("total_value").path("breakdowns").get(0).path("results");
            for (JsonNode breakdown : results) {
                String key = breakdown.path("dimension_values").get(0).asText();
                int value = breakdown.path("value").asInt();
                result.put(key, value);
            }
        } catch (Exception e) {
            log.warn("Breakdown 값 추출 실패: {}", keyName, e);
        }
        return result;
    }
}
