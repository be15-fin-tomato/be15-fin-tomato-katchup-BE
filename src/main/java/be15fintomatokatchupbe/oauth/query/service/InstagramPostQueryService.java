package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.dashboard.query.external.OpenAiClient;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Instagram;
import be15fintomatokatchupbe.influencer.command.domain.repository.InstagramRepository;
import be15fintomatokatchupbe.infra.redis.InstagramTokenRepository;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
import be15fintomatokatchupbe.oauth.query.dto.InstagramCommentInfo;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramPostInsightResponse;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import be15fintomatokatchupbe.relation.repository.PipeInfClientManagerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstagramPostQueryService {

    private final WebClient webClient;
    private final InstagramTokenRepository instagramTokenRepository;
    private final PipeInfClientManagerRepository picmRepository;
    private final InstagramRepository instagramRepository;
    private final OpenAiClient openAiClient;


    @Value("${facebook.base-url}")
    private String baseUrl;

    public InstagramPostInsightResponse fetchPostInsightsByPipelineInfluencerId(Long pipelineInfluencerId) {
        PipelineInfluencerClientManager picm = picmRepository.findById(pipelineInfluencerId)
                .orElseThrow(() -> new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND));

        String permalink = picm.getInstagramLink();
        Long influencerId = picm.getInfluencer().getId();

        String igUserId = instagramRepository.findById(influencerId)
                .map(Instagram::getAccountId)
                .orElseThrow(() -> new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND));

        String accessToken = instagramTokenRepository.getAccessToken(igUserId);
        if (accessToken == null) {
            log.warn("[InstagramPostQueryService] Redis에 토큰 없음. igUserId={}", igUserId);
            throw new BusinessException(OAuthErrorCode.TOKEN_NOT_FOUND);
        }

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

        List<InstagramCommentInfo> commentContent = fetchCommentsByMediaId(mediaId, accessToken);
        return parseInsightResponse(insights, permalink, commentContent);
    }

    private InstagramPostInsightResponse parseInsightResponse(JsonNode insights, String permalink, List<InstagramCommentInfo> commentContent) {
        InstagramPostInsightResponse.InstagramPostInsightResponseBuilder builder = InstagramPostInsightResponse.builder()
                .permalink(permalink)
                .commentContent(commentContent);

        for (JsonNode metric : insights.path("data")) {
            String name = metric.path("name").asText();
            int value = metric.path("values").get(0).path("value").asInt(0);

            switch (name) {
                case "views": builder.views(value); break;
                case "reach": builder.reach(value); break;
                case "likes": builder.likes(value); break;
                case "comments": builder.comments(value); break;
                case "shares": builder.shares(value); break;
                case "saved": builder.saved(value); break;
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
        return switch (mediaType.toUpperCase()) {
            case "VIDEO" -> "views,reach,likes,comments,shares,saved";
            case "IMAGE", "CAROUSEL_ALBUM" -> "saved,follows,likes,comments,reach";
            case "REEL" -> "views,likes,comments,shares,saved";
            default -> "likes,comments";
        };
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

    public List<InstagramCommentInfo> fetchCommentsByMediaId(String mediaId, String accessToken) {
        String url = String.format("%s/%s/comments?fields=id,text,like_count&access_token=%s", baseUrl, mediaId, accessToken);
        JsonNode response = webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class).block();

        if (response == null || !response.has("data")) {
            log.warn("[InstagramPostQueryService] No comments found for mediaId={}", mediaId);
            return List.of();
        }

        List<InstagramCommentInfo> comments = new ArrayList<>();
        for (JsonNode comment : response.get("data")) {
            String text = comment.path("text").asText("");
            int likeCount = comment.path("like_count").asInt(0);

            if (!text.isEmpty()) {
                comments.add(new InstagramCommentInfo(text, likeCount));
            }
        }

        // 좋아요 내림차순 정렬
        comments.sort((a, b) -> Integer.compare(b.getLikeCount(), a.getLikeCount()));

        return comments;
    }

    public String summarizeInstagramCommentsByPipelineInfluencerId(Long pipelineInfluencerId) {
        PipelineInfluencerClientManager picm = picmRepository.findById(pipelineInfluencerId)
                .orElseThrow(() -> new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND));

        String permalink = picm.getInstagramLink();
        Long influencerId = picm.getInfluencer().getId();

        String igUserId = instagramRepository.findById(influencerId)
                .map(Instagram::getAccountId)
                .orElseThrow(() -> new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND));

        String accessToken = instagramTokenRepository.getAccessToken(igUserId);
        if (accessToken == null) {
            log.warn("[InstagramPostQueryService] Redis에 토큰 없음. igUserId={}", igUserId);
            throw new BusinessException(OAuthErrorCode.TOKEN_NOT_FOUND);
        }

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

        List<InstagramCommentInfo> commentList = fetchCommentsByMediaId(mediaId, accessToken);
        List<String> commentTexts = commentList.stream()
                .map(InstagramCommentInfo::getText)
                .toList();

        return openAiClient.summarizeComments(commentTexts);
    }

//    // ✅ 테스트용 강제 토큰 저장 메서드
//    public void forceSaveToken(Long influencerId, String token) {
//        String igUserId = instagramRepository.findById(influencerId)
//                .map(Instagram::getAccountId)
//                .orElseThrow(() -> new BusinessException(OAuthErrorCode.MEDIA_NOT_FOUND));
//
//        instagramRedisService.saveAccessToken(igUserId, token, 60 * 60 * 24 * 60); // 60일 저장
//        log.info("✅ [forceSaveToken] 토큰 저장 완료: igUserId={}, token={}", igUserId, token);
//    }
}
