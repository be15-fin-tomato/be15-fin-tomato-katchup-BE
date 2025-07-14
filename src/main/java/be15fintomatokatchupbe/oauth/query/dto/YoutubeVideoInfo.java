package be15fintomatokatchupbe.oauth.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class YoutubeVideoInfo {
    public Long influencerId;
    private String title;
    private String videoId;
    private Long views;
    private Long likes;
    private Long comments;
    private String thumbnailUrl;
    private LocalDateTime publishedAt;
}
