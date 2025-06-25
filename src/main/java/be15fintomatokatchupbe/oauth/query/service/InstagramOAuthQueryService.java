package be15fintomatokatchupbe.oauth.query.service;

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
public class InstagramOAuthQueryService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL = "https://graph.facebook.com/v23.0";
    private static final String IG_ACCOUNT_ID = "17841472994141687"; // 사용자 계정 ID 고정 또는 인자로 받아도 됨

    public int fetchTotalViews(String accessToken, LocalDate since, LocalDate until) {
        String url = buildUrl("/insights", Map.of(
                "metric", "views",
                "period", "day",
                "metric_type", "total_value",
                "since", toUnix(since),
                "until", toUnix(until),
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        return sumMetricValues(root, "views");
    }

    public Map<String, Integer> fetchViewsByFollowType(String accessToken, LocalDate since, LocalDate until) {
        String url = buildUrl("/insights", Map.of(
                "metric", "views",
                "period", "day",
                "metric_type", "total_value",
                "breakdown", "follow_type",
                "since", toUnix(since),
                "until", toUnix(until),
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        return extractBreakdownValues(root, "views", "follow_type");
    }

    public Map<String, Integer> fetchFollowerGenderRatio(String accessToken) {
        String url = buildUrl("/insights", Map.of(
                "metric", "follower_demographics",
                "period", "lifetime",
                "timeframe", "this_month",
                "metric_type", "total_value",
                "breakdown", "gender",
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        return extractBreakdownValues(root, "follower_demographics", "gender");
    }

    public Map<String, Integer> fetchFollowerAgeRatio(String accessToken) {
        String url = buildUrl("/insights", Map.of(
                "metric", "follower_demographics",
                "period", "lifetime",
                "timeframe", "this_month",
                "metric_type", "total_value",
                "breakdown", "age",
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        return extractBreakdownValues(root, "follower_demographics", "age");
    }

    public List<Integer> fetchDailyFollowerCounts(String accessToken, LocalDate since, LocalDate until) {
        String url = buildUrl("/insights", Map.of(
                "metric", "follower_count",
                "period", "day",
                "since", toUnix(since),
                "until", toUnix(until),
                "access_token", accessToken
        ));

        JsonNode root = getJson(url);
        return extractDailyValues(root, "follower_count");
    }

    private String buildUrl(String path, Map<String, String> params) {
        StringBuilder url = new StringBuilder(BASE_URL + "/" + IG_ACCOUNT_ID + path + "?");
        for (var entry : params.entrySet()) {
            url.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
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

    private int sumMetricValues(JsonNode root, String metricName) {
        try {
            JsonNode values = root.path("data").findValue("total_value");
            return values != null && values.has("value") ? values.get("value").asInt() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private Map<String, Integer> extractBreakdownValues(JsonNode root, String metricName, String keyName) {
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

    private List<Integer> extractDailyValues(JsonNode root, String metricName) {
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
