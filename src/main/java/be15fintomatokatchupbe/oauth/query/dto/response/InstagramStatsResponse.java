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
    // 핵심 수치
    private Double dailyAverageViews;     // 최근 7일 일 평균 조회수
    private Double monthlyAverageViews;   // 최근 30일 월 평균 조회수
    private int totalFollowers;           // 전체 팔로워 수

    // 비율 분포
    private Map<String, Double> followerAgeDistribution;     // 연령대별 비율
    private Map<String, Double> followerGenderDistribution;  // 성별 비율
    private Double followerRatio;         // 팔로워 비율 (0~100)
    private Double nonFollowerRatio;      // 비팔로워 비율 (0~100)

    // 팔로워 성장률
    private Double dailyFollowerGrowthRate;    // 하루 전 대비 변화율 (%)
    private Double weeklyFollowerGrowthRate;   // 7일 전 대비 변화율 (%)
    private Double monthlyFollowerGrowthRate;  // 30일 전 대비 변화율 (%)

    // 미디어 리스트
    private List<InstagramMediaStats> topPosts;         // 인기 게시물 (IMAGE / CAROUSEL)
    private List<InstagramMediaStats> topVideos;        // 인기 영상 (VIDEO / REELS)
    private List<InstagramMediaStats> allMediaStats;    // 전체 게시물 리스트

    private int totalPosts;     // 총 게시글 수

    // 전체 평균 수치
    private Double averageViews;
    private Double averageLikes;
    private Double averageComments;
}
