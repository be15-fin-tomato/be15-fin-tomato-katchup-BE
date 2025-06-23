package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeOAuthQueryService {

    @Value("${youtube.client-id}")
    private String clientId;

    @Value("${youtube.client-secret}")
    private String clientSecret;

    @Value("${youtube.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient = WebClient.create();

    public String buildAuthorizationUrl() {
        return UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", List.of(
                        "https://www.googleapis.com/auth/youtube.readonly",
                        "https://www.googleapis.com/auth/yt-analytics.readonly"
                )))
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .toUriString();
    }

    public GoogleTokenResponse getAccessToken(String code) {
        return postForm("https://oauth2.googleapis.com/token", buildForm(code), GoogleTokenResponse.class);
    }

    public ChannelIdResponse getMyChannelId(String accessToken) {
        String url = "https://www.googleapis.com/youtube/v3/channels?part=id&mine=true";
        return getWithAuth(url, accessToken, ChannelIdResponse.class);
    }

    public AnalyticsResponse getChannelAnalytics(String accessToken, String channelId, String startDate, String endDate,
                                                 String metrics, String dimensions, String filters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://youtubeanalytics.googleapis.com/v2/reports")
                .queryParam("ids", "channel==" + channelId)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("metrics", metrics)
                .queryParam("dimensions", dimensions.trim())
                .queryParam("sort", dimensions.trim());

        if (filters != null && !filters.isBlank()) {
            builder.queryParam("filters", filters);
        }

        String uri = builder.build(false).toUriString();
        log.info("[YT API] {}", uri);

        try {
            return getWithAuth(uri, accessToken, AnalyticsResponse.class);
        } catch (WebClientResponseException e) {
            log.error("🔥 YouTube API 오류: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    public int getTotalVideoCount(String accessToken, String channelId) {
        String url = "https://www.googleapis.com/youtube/v3/channels?part=statistics&id=" + channelId;
        ChannelStatsResponse response = getWithAuth(url, accessToken, ChannelStatsResponse.class);
        return response.getItems().get(0).getStatistics().getVideoCount();
    }

    public long getMetricSum(String accessToken, String channelId, String startDate, String endDate, String metric) {
        try {
            AnalyticsResponse response = getChannelAnalytics(accessToken, channelId, startDate, endDate, metric, "day", null);
            return response.getRows().stream().mapToLong(row -> ((Number) row.get(1)).longValue()).sum();
        } catch (Exception e) {
            log.warn("⚠️ metricSum 실패 - fallback 0 반환: {}", e.getMessage());
            return 0;
        }
    }

    public int getDateRangeInDays(String startDate, String endDate) {
        return (int) ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate)) + 1;
    }

    public Map<String, Double> getRatioByDimension(String accessToken, String channelId, String startDate,
                                                   String endDate, String dimension) {
        try {
            AnalyticsResponse response = getChannelAnalytics(accessToken, channelId, startDate, endDate,
                    "viewerPercentage", dimension, null);
            return response.getRows().stream().collect(Collectors.toMap(
                    row -> row.get(0).toString(),
                    row -> ((Number) row.get(1)).doubleValue()
            ));
        } catch (Exception e) {
            log.warn("⚠️ getRatioByDimension 실패 - fallback: {}", e.getMessage());
            return Map.of();
        }
    }

    public Map<String, Integer> getSubscriberChanges(String accessToken, String channelId) {
        Map<String, Integer> changes = new HashMap<>();
        changes.put("daily", getNetSubscribers(accessToken, channelId, 1));
        changes.put("weekly", getNetSubscribers(accessToken, channelId, 7));
        changes.put("monthly", getNetSubscribers(accessToken, channelId, 30));
        return changes;
    }

    private int getNetSubscribers(String accessToken, String channelId, int daysAgo) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(daysAgo);
        try {
            AnalyticsResponse response = getChannelAnalytics(accessToken, channelId, start.toString(), end.toString(),
                    "subscribersGained,subscribersLost", "day", null);
            int gained = response.getRows().stream().mapToInt(r -> ((Number) r.get(1)).intValue()).sum();
            int lost = response.getRows().stream().mapToInt(r -> ((Number) r.get(2)).intValue()).sum();
            return gained - lost;
        } catch (Exception e) {
            log.warn("⚠️ getNetSubscribers 실패 - fallback 0 반환: {}", e.getMessage());
            return 0;
        }
    }

    public Map<String, Double> getSubscribedStatusRatio(String accessToken, String channelId, String startDate, String endDate) {
        try {
            AnalyticsResponse response = getChannelAnalytics(accessToken, channelId, startDate, endDate,
                    "views", "subscribedStatus", null);
            long totalViews = response.getRows().stream().mapToLong(r -> ((Number) r.get(1)).longValue()).sum();
            if (totalViews == 0) return Map.of("subscribed", 0.0, "notSubscribed", 0.0);

            return response.getRows().stream().collect(Collectors.toMap(
                    row -> row.get(0).toString().equals("SUBSCRIBED") ? "subscribed" : "notSubscribed",
                    row -> ((Number) row.get(1)).doubleValue() * 100 / totalViews
            ));
        } catch (WebClientResponseException e) {
            log.warn("⚠️ subscribedStatus 불가 - fallback to empty result: {}", e.getResponseBodyAsString());
            return Map.of("subscribed", 0.0, "notSubscribed", 0.0);
        }
    }

    public Map<String, Double> getAgeGroupRatio(String accessToken, String channelId, String startDate, String endDate) {
        return getRatioByDimension(accessToken, channelId, startDate, endDate, "ageGroup");
    }

    public Map<String, Double> getGenderRatio(String accessToken, String channelId, String startDate, String endDate) {
        return getRatioByDimension(accessToken, channelId, startDate, endDate, "gender");
    }

    public long getTotalViews(String accessToken, String channelId, String startDate, String endDate) {
        return getMetricSum(accessToken, channelId, startDate, endDate, "views");
    }

    public long getTotalLikes(String accessToken, String channelId, String startDate, String endDate) {
        return getMetricSum(accessToken, channelId, startDate, endDate, "likes");
    }

    public long getTotalComments(String accessToken, String channelId, String startDate, String endDate) {
        return getMetricSum(accessToken, channelId, startDate, endDate, "comments");
    }

    public List<YoutubeStatsResponse.VideoInfo> getTopVideos(String accessToken, String channelId, String startDate, String endDate) {
        try {
            AnalyticsResponse response = getChannelAnalytics(accessToken, channelId, startDate, endDate, "views", "video", null);
            return response.getRows().stream()
                    .sorted(Comparator.comparingLong(row -> -((Number) row.get(1)).longValue()))
                    .limit(5)
                    .map(row -> YoutubeStatsResponse.VideoInfo.builder()
                            .videoId(row.get(0).toString())
                            .views(((Number) row.get(1)).longValue())
                            .title("(제목 조회 필요)")
                            .thumbnailUrl("(썸네일 조회 필요)")
                            .build())
                    .toList();
        } catch (Exception e) {
            log.warn("⚠️ getTopVideos 실패 - fallback: {}", e.getMessage());
            return List.of();
        }
    }

    public List<YoutubeStatsResponse.VideoInfo> getTopShorts(String accessToken, String channelId, String startDate, String endDate) {
        return getTopVideos(accessToken, channelId, startDate, endDate).stream()
                .filter(v -> v.getTitle().toLowerCase().contains("short") || v.getTitle().contains("쇼츠"))
                .toList();
    }

    private <T> T getWithAuth(String url, String accessToken, Class<T> responseType) {
        String token = accessToken.startsWith("Bearer ") ? accessToken : "Bearer " + accessToken;
        log.info("✅ 최종 Authorization 헤더: {}", token);

        return webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    private <T> T postForm(String url, MultiValueMap<String, String> body, Class<T> responseType) {
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    private MultiValueMap<String, String> buildForm(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "authorization_code");
        return map;
    }

    @Getter
    @Setter
    public static class GoogleTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
    }

    @Getter
    @Setter
    public static class ChannelIdResponse {
        private List<Item> items;

        @Getter
        @Setter
        public static class Item {
            private String id;
        }
    }

    @Getter
    @Setter
    public static class ChannelStatsResponse {
        private List<Item> items;

        @Getter
        @Setter
        public static class Item {
            private Statistics statistics;
        }

        @Getter
        @Setter
        public static class Statistics {
            @JsonProperty("videoCount")
            private int videoCount;
        }
    }

    @Getter
    @Setter
    public static class AnalyticsResponse {
        private List<ColumnHeader> columnHeaders;
        private List<List<Object>> rows;

        @Getter
        @Setter
        public static class ColumnHeader {
            private String name;
            private String columnType;
            private String dataType;
        }
    }
}
