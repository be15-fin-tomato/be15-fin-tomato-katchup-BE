package be15fintomatokatchupbe.oauth.command.application.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
