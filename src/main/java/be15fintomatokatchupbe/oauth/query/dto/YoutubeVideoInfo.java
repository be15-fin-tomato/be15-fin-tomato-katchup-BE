package be15fintomatokatchupbe.oauth.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class YoutubeVideoInfo {
    private String title;
    private String videoId;
    private long views;
    private long likes;
    private long comments;
    private String thumbnailUrl;
}
