package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import be15fintomatokatchupbe.oauth.query.dto.request.InstagramPermalinkRequest;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramPostInsightResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstagramPostQueryService {

    private final WebClient webClient;

    @Value("${facebook.base-url}")
    private String baseUrl;

    public InstagramPostInsightResponse fetchPostInsightsByPermalink(InstagramPermalinkRequest request) {
        String accessToken = request.getAccessToken();
        String igUserId = request.getIgUserId();
        String permalink = request.getPermalink();

        String shortcode = extractShortcode(permalink);
        if (shortcode == null) {
            log.warn("[InstagramPostQueryService] Invalid permalink: {}", permalink);
            throw new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND);
        }

        String mediaId = findMatchingMediaId(igUserId, accessToken, shortcode);
        if (mediaId == null) {
            log.warn("[InstagramPostQueryService] No media found for shortcode: {}", shortcode);
            throw new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND);
        }

        String mediaType = fetchMediaType(mediaId, accessToken);
        if (!isSupportedMediaType(mediaType)) {
            log.warn("[InstagramPostQueryService] Unsupported media_type: {}", mediaType);
            throw new BusinessException(OAuthErrorCode.UNSUPPORTED_MEDIA_TYPE);
        }

        String metrics = buildMetrics(mediaType);
        JsonNode insights = fetchInsights(mediaId, accessToken, metrics);

        return parseInsightResponse(insights, permalink);
    }

    private InstagramPostInsightResponse parseInsightResponse(JsonNode insights, String permalink) {
        InstagramPostInsightResponse.InstagramPostInsightResponseBuilder builder = InstagramPostInsightResponse.builder()
                .permalink(permalink);

        for (JsonNode metric : insights.path("data")) {
            String name = metric.path("name").asText();
            int value = metric.path("values").get(0).path("value").asInt(0);

            switch (name) {
                case "views":
                    builder.views(value); break;
                case "reach":
                    builder.reach(value); break;
                case "likes":
                    builder.likes(value); break;
                case "comments":
                    builder.comments(value); break;
                case "shares":
                    builder.shares(value); break;
                case "saved":
                    builder.saved(value); break;
            }
        }

        return builder.build();
    }

    private String extractShortcode(String permalink) {
        Pattern pattern = Pattern.compile("https://www\\.instagram\\.com/(?:p|reel)/([a-zA-Z0-9_-]+)/?");
        Matcher matcher = pattern.matcher(permalink);
        return matcher.find() ? matcher.group(1) : null;
    }


    private String findMatchingMediaId(String igUserId, String accessToken, String shortcode) {
        String url = String.format("%s/%s/media?fields=id,permalink&access_token=%s", baseUrl, igUserId, accessToken);
        JsonNode mediaList = webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class).block();

        if (mediaList == null || !mediaList.has("data")) {
            return null;
        }

        for (JsonNode media : mediaList.get("data")) {
            if (media.path("permalink").asText().contains(shortcode)) {
                return media.path("id").asText();
            }
        }
        return null;
    }

    private String fetchMediaType(String mediaId, String accessToken) {
        String url = String.format("%s/%s?fields=media_type&access_token=%s", baseUrl, mediaId, accessToken);
        JsonNode response = webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class).block();
        return response != null ? response.path("media_type").asText() : "";
    }

    private boolean isSupportedMediaType(String mediaType) {
        return "IMAGE".equalsIgnoreCase(mediaType)
                || "VIDEO".equalsIgnoreCase(mediaType)
                || "CAROUSEL_ALBUM".equalsIgnoreCase(mediaType)
                || "REEL".equalsIgnoreCase(mediaType); // 일부 미디어에서 REEL로 나타날 수 있음
    }

    private String buildMetrics(String mediaType) {
        switch (mediaType.toUpperCase()) {
            case "VIDEO":
                return "views,reach,likes,comments,shares,saved";
            case "IMAGE":
            case "CAROUSEL_ALBUM":
                return "saved,follows,likes,comments,reach";
            case "REEL":
                return "views,likes,comments,shares,saved";
            default:
                return "likes,comments";
        }
    }

    private JsonNode fetchInsights(String mediaId, String accessToken, String metrics) {
        try {
            String url = String.format("%s/%s/insights?metric=%s&access_token=%s", baseUrl, mediaId, metrics, accessToken);
            return webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class).block();
        } catch (Exception e) {
            log.error("[InstagramPostQueryService] Failed to fetch insights", e);
            throw new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND);
        }
    }
}
