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
    private Double dailyAverageViews;                    // 최근 7일 일 평균 조회수
    private Double monthlyAverageViews;                  // 최근 30일 월 평균 조회수
    private Double followerRatio;                        // 팔로워 비율 (0~100)
    private Double nonFollowerRatio;                     // 비팔로워 비율 (0~100)
    private Map<String, Double> followerAgeDistribution; // 연령대별 비율 (예: "18-24": 35.0)
    private Map<String, Double> followerGenderDistribution; // 성별 비율 ("FEMALE": 65.0)
    private Double dailyFollowerGrowthRate;              // 하루 전 대비 팔로워 변화율 (%)
    private Double weeklyFollowerGrowthRate;             // 7일 전 대비 팔로워 변화율 (%)
    private Double monthlyFollowerGrowthRate;            // 30일 전 대비 팔로워 변화율 (%)
    private List<InstagramMediaStats> topPosts;          // 인기 게시물 (IMAGE / CAROUSEL)
    private List<InstagramMediaStats> topVideos;         // 인기 영상 (VIDEO / REELS)
    private int totalFollowers;                          // 전체 팔로워 수
}
