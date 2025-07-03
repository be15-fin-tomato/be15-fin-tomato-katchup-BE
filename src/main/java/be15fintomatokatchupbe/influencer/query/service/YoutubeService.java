package be15fintomatokatchupbe.influencer.query.service;

import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class YoutubeService {

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public YoutubeChannelInfo fetchChannelInfo(String channelId) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/channels")
                .queryParam("part", "snippet,statistics")
                .queryParam("id", channelId)
                .queryParam("key", youtubeApiKey)
                .toUriString();

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();

            if (body == null || !body.containsKey("items")) {
                throw new BusinessException(InfluencerErrorCode.YOUTUBE_ACCOUNT_NOT_FOUND);
            }

            var items = (java.util.List<?>) body.get("items");
            if (items.isEmpty()) {
                throw new BusinessException(InfluencerErrorCode.YOUTUBE_ACCOUNT_NOT_FOUND);
            }

            Map<String, Object> item = (Map<String, Object>) items.get(0);
            Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
            Map<String, Object> statistics = (Map<String, Object>) item.get("statistics");

            String title = (String) snippet.get("title");
            String thumbnail = extractThumbnailUrl(snippet);
            long subscriberCount = Long.parseLong((String) statistics.get("subscriberCount"));

            return new YoutubeChannelInfo(title, thumbnail, subscriberCount);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("유튜브 API 호출 실패", e);
            throw new BusinessException(InfluencerErrorCode.FAILED_TO_FETCH_YOUTUBE_DATA);
        }
    }

    public static String extractVideoId(String youtubeUrl) {
        try {
            if (youtubeUrl == null || !youtubeUrl.contains("watch?v=")) {
                throw new BusinessException(InfluencerErrorCode.YOUTUBE_VIDEO_NOT_FOUND);
            }
            return youtubeUrl.split("watch\\?v=")[1].split("&")[0];
        } catch (Exception e) {
            throw new BusinessException(InfluencerErrorCode.YOUTUBE_VIDEO_NOT_FOUND);
        }
    }

    public String getUploadDate(String videoId) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/videos")
                .queryParam("part", "snippet")
                .queryParam("id", videoId)
                .queryParam("key", youtubeApiKey)
                .toUriString();

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();

            if (body == null || !body.containsKey("items")) {
                throw new BusinessException(InfluencerErrorCode.YOUTUBE_VIDEO_NOT_FOUND);
            }

            var items = (java.util.List<?>) body.get("items");
            if (items.isEmpty()) {
                throw new BusinessException(InfluencerErrorCode.YOUTUBE_VIDEO_NOT_FOUND);
            }

            Map<String, Object> item = (Map<String, Object>) items.get(0);
            Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");

            String publishedAt = (String) snippet.get("publishedAt");

            // ISO 8601 형식 문자열을 LocalDateTime으로 변환 후 날짜만 반환
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime uploadDateTime = LocalDateTime.parse(publishedAt, formatter);

            // Convert LocalDate to String
            return uploadDateTime.toLocalDate().toString(); // return the date as a string

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("유튜브 업로드 날짜 조회 실패", e);
            throw new BusinessException(InfluencerErrorCode.FAILED_TO_FETCH_YOUTUBE_DATA);
        }
    }

    public Map<String, Long> getVideoMetrics(String videoId) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/videos")
                .queryParam("part", "statistics")
                .queryParam("id", videoId)
                .queryParam("key", youtubeApiKey)
                .toUriString();

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();

            if (body == null || !body.containsKey("items")) {
                throw new BusinessException(InfluencerErrorCode.YOUTUBE_VIDEO_NOT_FOUND);
            }

            var items = (java.util.List<?>) body.get("items");
            if (items.isEmpty()) {
                throw new BusinessException(InfluencerErrorCode.YOUTUBE_VIDEO_NOT_FOUND);
            }

            Map<String, Object> item = (Map<String, Object>) items.get(0);
            Map<String, Object> statistics = (Map<String, Object>) item.get("statistics");

            // 원하는 메트릭들 추출
            Map<String, Long> metrics = new HashMap<>();
            metrics.put("commentCount", Long.parseLong((String) statistics.getOrDefault("commentCount", "0")));
            metrics.put("likeCount", Long.parseLong((String) statistics.getOrDefault("likeCount", "0")));
            metrics.put("viewCount", Long.parseLong((String) statistics.getOrDefault("viewCount", "0")));

            return metrics;

        } catch (Exception e) {
            log.error("유튜브 영상 메트릭 조회 실패", e);
            throw new BusinessException(InfluencerErrorCode.FAILED_TO_FETCH_YOUTUBE_DATA);
        }
    }

    private String extractThumbnailUrl(Map<String, Object> snippet) {
        try {
            Map<String, Object> thumbnails = (Map<String, Object>) snippet.get("thumbnails");
            if (thumbnails == null) return null;

            if (thumbnails.containsKey("high")) {
                return (String) ((Map<String, Object>) thumbnails.get("high")).get("url");
            } else if (thumbnails.containsKey("medium")) {
                return (String) ((Map<String, Object>) thumbnails.get("medium")).get("url");
            } else if (thumbnails.containsKey("default")) {
                return (String) ((Map<String, Object>) thumbnails.get("default")).get("url");
            }
        } catch (Exception e) {
            log.warn("썸네일 파싱 실패: {}", e.getMessage());
        }
        return null;
    }
    public List<String> getCommentsByVideoId(String videoId) {
        try {
            log.info("Fetching YouTube comments for videoId: {}", videoId);

            YouTube youtubeService = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            ).setApplicationName("youtube-comments-fetcher")
                    .setYouTubeRequestInitializer(new YouTubeRequestInitializer(youtubeApiKey))
                    .build();

            YouTube.CommentThreads.List request = youtubeService.commentThreads()
                    .list("snippet")
                    .setVideoId(videoId)
                    .setTextFormat("plainText")
                    .setOrder("relevance")
                    .setMaxResults(50L);

            CommentThreadListResponse response = request.execute();

            return response.getItems().stream()
                    .map(thread -> thread.getSnippet().getTopLevelComment().getSnippet().getTextDisplay())
                    .collect(Collectors.toList());

        } catch (IOException | GeneralSecurityException e) {
            log.error("댓글 조회 실패 (videoId: {})", videoId, e);
            throw new BusinessException(InfluencerErrorCode.FAILED_TO_FETCH_YOUTUBE_DATA);
        } catch (Exception e) {
            log.error("알 수 없는 예외 발생 (videoId: {})", videoId, e);
            throw new BusinessException(InfluencerErrorCode.FAILED_TO_FETCH_YOUTUBE_DATA);
        }
    }


    public record YoutubeChannelInfo(String channelName, String thumbnailUrl, Long subscriberCount) {}
}
