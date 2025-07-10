package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.application.support.YoutubeHelperService;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.infra.redis.YoutubeTokenRepository;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import be15fintomatokatchupbe.oauth.query.dto.YoutubeVideoInfo;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeChannelInfoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
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

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeOAuthQueryService {
    private final YoutubeTokenRepository youtubeTokenRepository;
    private final YoutubeHelperService youtubeHelperService;

    private final WebClient webClient;
    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.client-id}")
    private String clientId;

    @Value("${youtube.client-secret}")
    private String clientSecret;

    @Value("${youtube.redirect-uri}")
    private String redirectUri;

    // 유튜브 인증 URL 생성
    public String buildAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", List.of(
                        "https://www.googleapis.com/auth/youtube.readonly",
                        "https://www.googleapis.com/auth/yt-analytics.readonly"
                )))
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build()
                .toUriString();
    }

    // code → accessToken + refreshToken 교환
    public GoogleTokenResponse getToken(String code) {
        try {
            return postForm("https://oauth2.googleapis.com/token", buildForm(code), GoogleTokenResponse.class);
        } catch (WebClientResponseException e) {
            log.error("🔥 Google 토큰 요청 실패: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    // 최초 연동: 채널 정보 조회 후 DB 저장
    public void registerYoutubeAccount(Long influencerId, String accessToken, String refreshToken) {
        // 1. 채널 정보 조회
        String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&mine=true";
        YoutubeChannelInfoResponse youtubeChannelInfo = getWithAuth(url, accessToken, YoutubeChannelInfoResponse.class);

        if (youtubeChannelInfo.items() == null || youtubeChannelInfo.items().isEmpty()) {
            throw new BusinessException(OAuthErrorCode.YOUTUBE_CHANNEL_NOT_FOUND);
        }

        YoutubeChannelInfoResponse.Item item = youtubeChannelInfo.items().get(0);

        // 2. 정보 추출
        String channelId = item.id();
        String title = item.snippet().title();
        String thumbnail = item.snippet().thumbnails().defaultThumbnail().url();
        Long subscriberCount = item.statistics().subscriberCount();

        // 3. 유튜브 엔티티 저장
        Youtube youtube = Youtube.builder()
                .influencerId(influencerId)
                .channelId(channelId)
                .title(title)
                .thumbnail(thumbnail)
                .refreshToken(refreshToken)
                .subscriber(subscriberCount)
                .build();

        youtubeHelperService.save(youtube);
        log.info("✅ 유튜브 계정 연동 완료 - influencerId={}, channelId={}", influencerId, channelId);
    }

    public void registerYoutubeByOAuth(String code, Long influencerId) {
        GoogleTokenResponse tokenResponse = getToken(code);
        saveRefreshTokenByAccess(tokenResponse);
        registerYoutubeAccount(influencerId, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

    private void saveRefreshTokenByAccess(GoogleTokenResponse tokenResponse) {
        String accessToken = tokenResponse.getAccessToken();
        YoutubeChannelInfoResponse youtubeChannelInfo = getWithAuth(
                "https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics&mine=true",
                accessToken,
                YoutubeChannelInfoResponse.class
        );
        if (youtubeChannelInfo.items() == null || youtubeChannelInfo.items().isEmpty()) {
            throw new BusinessException(OAuthErrorCode.YOUTUBE_CHANNEL_NOT_FOUND);
        }
        String channelId = youtubeChannelInfo.items().get(0).id();
        Duration duration = Duration.ofSeconds(tokenResponse.getExpiresIn());
        youtubeTokenRepository.save(channelId, tokenResponse.getRefreshToken());
        youtubeTokenRepository.saveAccessToken(channelId, tokenResponse.getAccessToken(), duration);
    }

    public ChannelIdResponse getMyChannelIdPost(String accessToken) {
        String url = "https://www.googleapis.com/youtube/v3/channels?part=id&mine=true";
        return getWithAuthPost(url, accessToken, ChannelIdResponse.class);
    }

    public AnalyticsResponse getChannelAnalytics(String accessToken, String channelId, String startDate, String endDate,
                                                 String metrics, String dimensions, String filters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://youtubeanalytics.googleapis.com/v2/reports")
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
            return getWithAuth(uri,accessToken, AnalyticsResponse.class);
        } catch (WebClientResponseException e) {
            log.error("🔥 YouTube API 오류: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    public int getTotalVideoCount(String accessToken, String channelId) {
        String url = "https://www.googleapis.com/youtube/v3/channels?part=statistics&id=" + channelId;
        ChannelStatsResponse response = getWithAuth(url,accessToken, ChannelStatsResponse.class);
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
            AnalyticsResponse response = getChannelAnalytics(
                    accessToken,
                    channelId,
                    startDate,
                    endDate,
                    "views",
                    "subscribedStatus",
                    null
            );

            long totalViews = response.getRows().stream()
                    .mapToLong(row -> ((Number) row.get(1)).longValue())
                    .sum();

            if (totalViews == 0) {
                return Map.of("subscribed", 0.0, "notSubscribed", 0.0);
            }

            return response.getRows().stream()
                    .collect(Collectors.toMap(
                            row -> {
                                String key = String.valueOf(row.get(0));
                                return switch (key) {
                                    case "SUBSCRIBED" -> "subscribed";
                                    case "UNSUBSCRIBED" -> "notSubscribed";
                                    default -> "unknown";
                                };
                            },
                            row -> ((Number) row.get(1)).doubleValue() * 100 / totalViews,
                            (v1, v2) -> v1 // ⚠ 중복 key 있을 경우 첫 번째 값 유지
                    ));

        } catch (WebClientResponseException e) {
            log.warn("⚠️ subscribedStatus 오류 - channelId={}, startDate={}, endDate={}, msg={}",
                    channelId, startDate, endDate, e.getMessage());
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

    public List<YoutubeVideoInfo> getTopVideos(String accessToken, String channelId, String startDate, String endDate) {
        try {
            // 1. 최신 영상 20개 조회
            URI searchUri = UriComponentsBuilder
                    .fromUriString("https://www.googleapis.com/youtube/v3/search")
                    .queryParam("part", "snippet")
                    .queryParam("channelId", channelId)
                    .queryParam("maxResults", 20)
                    .queryParam("order", "date")
                    .queryParam("type", "video")
                    .build()
                    .encode()
                    .toUri();

            JsonNode searchRoot = webClient.get()
                    .uri(searchUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            List<String> videoIds = new ArrayList<>();
            Map<String, JsonNode> snippetMap = new HashMap<>();
            for (JsonNode item : searchRoot.get("items")) {
                String videoId = item.get("id").get("videoId").asText();
                JsonNode snippet = item.get("snippet");
                videoIds.add(videoId);
                snippetMap.put(videoId, snippet);
            }

            if (videoIds.isEmpty()) return List.of();

            // 2. 영상 통계 조회 (v3/videos)
            URI statsUri = UriComponentsBuilder
                    .fromUriString("https://www.googleapis.com/youtube/v3/videos")
                    .queryParam("part", "statistics")
                    .queryParam("id", String.join(",", videoIds))
                    .queryParam("key", apiKey)
                    .build()
                    .encode()
                    .toUri();

            JsonNode statsRoot = webClient.get()
                    .uri(statsUri)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            List<YoutubeVideoInfo> videoInfos = new ArrayList<>();
            for (JsonNode item : Objects.requireNonNull(statsRoot).get("items")) {
                String videoId = item.get("id").asText();
                JsonNode statistics = item.get("statistics");
                JsonNode snippet = snippetMap.get(videoId);

                Long views = statistics.has("viewCount") ? statistics.get("viewCount").asLong() : 0;
                Long likes = statistics.has("likeCount") ? statistics.get("likeCount").asLong() : 0;
                long comments = statistics.has("commentCount") ? statistics.get("commentCount").asLong() : 0;

                LocalDateTime publishedAt = null;
                if (snippet != null && snippet.has("publishedAt")) {
                    try {
                        publishedAt = LocalDateTime.parse(snippet.get("publishedAt").asText().replace("Z", ""));
                    } catch (DateTimeParseException e) {
                        log.warn("Failed to parse publishedAt for video {}: {}", videoId, e.getMessage());
                    }
                }

                videoInfos.add(YoutubeVideoInfo.builder()
                        .videoId(videoId)
                        .views(views)
                        .comments(comments)
                        .likes(likes)
                        .title(snippet != null ? snippet.get("title").asText() : "(제목 없음)")
                        .thumbnailUrl(snippet != null ? snippet.get("thumbnails").get("default").get("url").asText() : null)
                        .publishedAt(publishedAt)
                        .build());
            }

            return videoInfos.stream()
                    .sorted(Comparator.comparingLong(YoutubeVideoInfo::getViews).reversed())
                    .limit(5)
                    .toList();

        } catch (Exception e) {
            log.warn("⚠️ getTopVideos 실패 - fallback: {}", e.getMessage());
            return List.of();
        }
    }


    public List<YoutubeVideoInfo> getTopShorts(String accessToken, String channelId, String startDate, String endDate) {
        return getTopVideos(accessToken, channelId, startDate, endDate).stream()
                .filter(v -> {
                    String title = v.getTitle().toLowerCase();
                    return title.contains("short") || title.contains("쇼츠") || title.contains("#shorts");
                })
                .toList();
    }

    private <T> T getWithAuthPost(String url, String accessToken, Class<T> responseType) {
        String token = accessToken.startsWith("Bearer ") ? accessToken : "Bearer " + accessToken;
        log.info("✅ 최종 Authorization 헤더: {}", token);

        return webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    private <T> T getWithAuth(String url, String accessToken, Class<T> responseType) {
        String token = accessToken.startsWith("Bearer ") ? accessToken : "Bearer " + accessToken;
        return webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    public String getValidAccessToken(String channelId) {
        // 1. Redis에서 accessToken 조회
        String accessToken = youtubeTokenRepository.findAccessToken(channelId);

        // 2. 없으면 refresh_token으로 accessToken 재발급
        if (accessToken == null || accessToken.isBlank()) {
            log.info("🔄 Redis에 accessToken 없음 → refresh token으로 재발급 시도");
            accessToken = refreshAndGetAccessToken(channelId);
        }

        return accessToken;
    }

    public String refreshAndGetAccessToken(String channelId) {
        String refreshToken = youtubeTokenRepository.find(channelId);
        if (refreshToken == null) {
            Youtube youtube = youtubeHelperService.findYoutube(channelId);
            refreshToken = youtube.getRefreshToken();
        }
        GoogleTokenResponse newToken = refreshAccessToken(refreshToken);
        Duration duration = Duration.ofSeconds(newToken.getExpiresIn());
        youtubeTokenRepository.saveAccessToken(channelId, newToken.getAccessToken(), duration);
        return newToken.getAccessToken();
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

    public GoogleTokenResponse refreshAccessToken(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("refresh_token", refreshToken);
        form.add("grant_type", "refresh_token");
        return postForm("https://oauth2.googleapis.com/token", form, GoogleTokenResponse.class);
    }

    @Getter
    public static class GoogleTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("expires_in")
        private int expiresIn;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("scope")
        private String scope;
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
