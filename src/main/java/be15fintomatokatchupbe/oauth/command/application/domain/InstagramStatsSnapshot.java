package be15fintomatokatchupbe.oauth.command.application.domain;

import be15fintomatokatchupbe.oauth.query.dto.response.InstagramStatsResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "instagram_stats_snapshot")
public class InstagramStatsSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long influencerId;

    private Integer totalPosts = 0;
    private Integer totalFollowers;

    private Double avgViews = 0.0;
    private Double avgLikes = 0.0;
    private Double avgComments = 0.0;
    private Double dailyAvgViews = 0.0;
    private Double monthlyAvgViews = 0.0;
    private Double reach = 0.0;

    private Double age1317 = 0.0;
    private Double age1824 = 0.0;
    private Double age2534 = 0.0;
    private Double age3544 = 0.0;
    private Double age4554 = 0.0;
    private Double age5564 = 0.0;
    private Double age65plus = 0.0;

    private Double genderFemale = 0.0;
    private Double genderMale = 0.0;

    private Double followerChangeDaily = 0.0;
    private Double followerChangeWeekly = 0.0;
    private Double followerChangeMonthly = 0.0;

    private Double followerRatio = 0.0;
    private Double nonFollowerRatio = 0.0;

    private LocalDate snapshotDate;

    public void update(InstagramStatsResponse stats) {
        this.reach = stats.getReach();
        this.totalPosts = stats.getTotalPosts();
        this.totalFollowers = stats.getTotalFollowers();
        this.avgViews = stats.getAverageViews();
        this.avgLikes = stats.getAverageLikes();
        this.avgComments = stats.getAverageComments();
        this.dailyAvgViews = stats.getDailyAverageViews();
        this.monthlyAvgViews = stats.getMonthlyAverageViews();
        this.genderFemale = stats.getGenderFemale();
        this.genderMale = stats.getGenderMale();
        this.followerChangeDaily = stats.getDailyFollowerGrowthRate();
        this.followerChangeWeekly = stats.getWeeklyFollowerGrowthRate();
        this.followerChangeMonthly = stats.getMonthlyFollowerGrowthRate();
        this.followerRatio = stats.getFollowerRatio();
        this.nonFollowerRatio = stats.getNonFollowerRatio();
    }

}
