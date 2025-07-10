package be15fintomatokatchupbe.oauth.query.dto.response;

import be15fintomatokatchupbe.oauth.query.dto.YoutubeVideoInfo;
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
    private SubscriberChangeResponse subscriberChange;
    private Map<String, Double> subscribedVsNot;
    private List<YoutubeVideoInfo> topVideos;
    private List<YoutubeVideoInfo> topShorts;
}


