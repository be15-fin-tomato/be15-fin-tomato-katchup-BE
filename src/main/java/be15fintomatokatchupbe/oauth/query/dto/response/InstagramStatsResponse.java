package be15fintomatokatchupbe.oauth.query.dto.response;

import be15fintomatokatchupbe.oauth.query.dto.InstagramMediaStats;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstagramStatsResponse {
    private Long influencerId;

    // 핵심 수치
    private int totalPosts;
    private Double dailyAverageViews;
    private Double monthlyAverageViews;
    private int totalFollowers;

    // 전체 미디어 평균 수치
    private Double averageViews;    // fetchStats의 avgViews
    private Double averageLikes;    // fetchStats의 avgLikes
    private Double averageComments; // fetchStats의 avgComments

    // 팔로워 성장률 (fetchStats의 growthRates Map에서 추출)
    private Double dailyFollowerGrowthRate;
    private Double weeklyFollowerGrowthRate;
    private Double monthlyFollowerGrowthRate;

    // 비율 분포
    private Map<String, Double> followerAgeDistribution;     // 연령대별 비율
    private Map<String, Double> followerGenderDistribution;  // 성별 비율

    private Double followerRatio;         // 팔로워 비율 (0~100)
    private Double nonFollowerRatio;      // 비팔로워 비율 (0~100)
    private Double followerChangeDailyRatio;

    private Double genderFemale;
    private Double genderMale;

    // 미디어 리스트
    private List<InstagramMediaStats> topPosts;         // 인기 게시물 (IMAGE / CAROUSEL)
    private List<InstagramMediaStats> topVideos;        // 인기 영상 (VIDEO / REELS)
    private List<InstagramMediaStats> allMediaStats;    // 전체 게시물 리스트
}
