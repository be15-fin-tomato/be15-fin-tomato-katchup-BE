package be15fintomatokatchupbe.oauth.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class YoutubeStatsResponse {
    private int totalVideos;
    private double avgViews;
    private double avgLikes;
    private double avgComments;
    private double dailyAvgViews;
    private double monthlyAvgViews;
    private Map<String, Double> subscriberAgeRatio;
    private Map<String, Double> subscriberGenderRatio;
    private Map<String, Integer> subscriberChange;
    private Map<String, Double> subscribedVsNot;
    private List<VideoInfo> topVideos;
    private List<VideoInfo> topShorts;

    @Getter
    @Builder
    public static class VideoInfo {
        private String title;
        private String videoId;
        private long views;
        private String thumbnailUrl;
    }
}

