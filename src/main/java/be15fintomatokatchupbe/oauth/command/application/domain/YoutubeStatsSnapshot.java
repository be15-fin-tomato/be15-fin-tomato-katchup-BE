package be15fintomatokatchupbe.oauth.command.application.domain;


import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "youtube_stats_snapshot")
public class YoutubeStatsSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long influencerId;

    private Integer totalVideos;
    private Double avgViews;
    private Double avgLikes;
    private Double avgComments;
    private Double dailyAvgViews;
    private Double monthlyAvgViews;

    private Double age1824;
    private Double age2534;
    private Double age3544;
    private Double age4554;

    private Double genderFemale;
    private Double genderMale;

    private Integer subscriberChangeDaily;
    private Integer subscriberChangeWeekly;
    private Integer subscriberChangeMonthly;

    private Double subscribedRatio;
    private Double notSubscribedRatio;

    private LocalDateTime createdAt;

    public static YoutubeStatsSnapshot from(Long influencerId, YoutubeStatsResponse response) {
        Map<String, Double> ageMap = response.getSubscriberAgeRatio();
        Map<String, Double> genderMap = response.getSubscriberGenderRatio();
        Map<String, Integer> changeMap = response.getSubscriberChange();
        Map<String, Double> subscribedMap = response.getSubscribedVsNot();

        return YoutubeStatsSnapshot.builder()
                .influencerId(influencerId)
                .totalVideos(response.getTotalVideos())
                .avgViews(response.getAvgViews())
                .avgLikes(response.getAvgLikes())
                .avgComments(response.getAvgComments())
                .dailyAvgViews(response.getDailyAvgViews())
                .monthlyAvgViews(response.getMonthlyAvgViews())
                .age1824(ageMap.getOrDefault("age18-24", 0.0))
                .age2534(ageMap.getOrDefault("age25-34", 0.0))
                .age3544(ageMap.getOrDefault("age35-44", 0.0))
                .age4554(ageMap.getOrDefault("age45-54", 0.0))
                .genderMale(genderMap.getOrDefault("male", 0.0))
                .genderFemale(genderMap.getOrDefault("female", 0.0))
                .subscriberChangeDaily(changeMap.getOrDefault("daily", 0))
                .subscriberChangeWeekly(changeMap.getOrDefault("weekly", 0))
                .subscriberChangeMonthly(changeMap.getOrDefault("monthly", 0))
                .subscribedRatio(subscribedMap.getOrDefault("subscribed", 0.0))
                .notSubscribedRatio(subscribedMap.getOrDefault("not_subscribed", 0.0))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public YoutubeStatsSnapshot updateFrom(YoutubeStatsResponse dto) {
        this.totalVideos = dto.getTotalVideos();
        this.avgViews = dto.getAvgViews();
        this.avgLikes = dto.getAvgLikes();
        this.avgComments = dto.getAvgComments();
        this.dailyAvgViews = dto.getDailyAvgViews();
        this.monthlyAvgViews = dto.getMonthlyAvgViews();
        this.age1824 = dto.getSubscriberAgeRatio().getOrDefault("age18-24", 0.0);
        this.age2534 = dto.getSubscriberAgeRatio().getOrDefault("age25-34", 0.0);
        this.age3544 = dto.getSubscriberAgeRatio().getOrDefault("age35-44", 0.0);
        this.age4554 = dto.getSubscriberAgeRatio().getOrDefault("age45-54", 0.0);
        this.genderFemale = dto.getSubscriberGenderRatio().getOrDefault("female", 0.0);
        this.genderMale = dto.getSubscriberGenderRatio().getOrDefault("male", 0.0);
        this.subscriberChangeDaily = dto.getSubscriberChange().getOrDefault("daily", 0);
        this.subscriberChangeWeekly = dto.getSubscriberChange().getOrDefault("weekly", 0);
        this.subscriberChangeMonthly = dto.getSubscriberChange().getOrDefault("monthly", 0);
        this.subscribedRatio = dto.getSubscribedVsNot().getOrDefault("subscribed", 0.0);
        this.notSubscribedRatio = dto.getSubscribedVsNot().getOrDefault("notSubscribed", 0.0);
        this.createdAt = LocalDateTime.now();
        return this;
    }


}
