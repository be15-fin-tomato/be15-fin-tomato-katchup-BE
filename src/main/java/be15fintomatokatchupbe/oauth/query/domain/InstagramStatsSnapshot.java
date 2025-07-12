package be15fintomatokatchupbe.oauth.query.domain;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
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

    @ManyToOne
    @JoinColumn(name = "influencer_id")
    private Influencer influencer;

    private Integer totalPosts;
    private Double avgViews;
    private Double avgLikes;
    private Double avgComments;
    private Double dailyAvgViews;
    private Double monthlyAvgViews;

    private Double age1317;
    private Double age1824;
    private Double age2534;
    private Double age3544;
    private Double age4554;
    private Double age5564;
    private Double age65plus;

    private Double genderFemale;
    private Double genderMale;

    private Double followerChangeDaily;
    private Double followerChangeWeekly;
    private Double followerChangeMonthly;

    private Double followerRatio;
    private Double nonFollowerRatio;

    private LocalDate snapshotDate;

    public void update(InstagramStatsResponse stats) {
        this.totalPosts = stats.getTotalPosts();
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
