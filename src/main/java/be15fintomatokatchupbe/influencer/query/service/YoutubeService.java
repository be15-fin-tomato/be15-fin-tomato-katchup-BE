package be15fintomatokatchupbe.influencer.query.service;

import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import java.util.Map;

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

    public record YoutubeChannelInfo(String channelName, String thumbnailUrl, Long subscriberCount) {}
}
