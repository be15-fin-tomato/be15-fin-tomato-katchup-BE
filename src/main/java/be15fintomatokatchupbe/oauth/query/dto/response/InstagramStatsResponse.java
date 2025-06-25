package be15fintomatokatchupbe.oauth.query.dto.response;

import be15fintomatokatchupbe.oauth.query.dto.InstagramMediaStats;
import lombok.*;

import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstagramStatsResponse {
    private Double dailyAverageViews;
    private Double monthlyAverageViews;
    private Double followerRatio; // % 값
    private Double nonFollowerRatio; // % 값
    private Map<String, Integer> followerAgeDistribution;
    private Map<String, Integer> followerGenderDistribution;
    private Double dailyFollowerGrowthRate;
    private Double weeklyFollowerGrowthRate;
    private Double monthlyFollowerGrowthRate;
    private List<InstagramMediaStats> topPosts;
    private List<InstagramMediaStats> topVideos;
}
