package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record CampaignContentResponse (
        String youtubeVideoId,      // 문자열 타입
        String videoThumbnailUrl,
        String channelThumbnailUrl,
        Map<String, Long> metrics
){}