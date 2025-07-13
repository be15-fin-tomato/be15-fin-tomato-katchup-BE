package be15fintomatokatchupbe.oauth.query.dto;

import be15fintomatokatchupbe.oauth.query.domain.InstagramMediaSnapshot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstagramFullSnapshot {
    private Long id;
    private Long influencerId;
    private int totalPosts;
    private Double avgLikes;
    private Double avgViews;
    private Double avgComments;
    private Double dailyAvgViews;
    private Double monthlyAvgViews;
    private int totalFollowers;
    private Double reach;
    private Double age1317;
    private Double age1824;
    private Double age2534;
    private Double age3544;
    private Double age4554;
    private Double age5564;
    private Double age65plus;
    private Double genderFemale;
    private Double genderMale;
    private Double genderUnknown;
    private Double followerChangeDaily;
    private Double followerChangeWeekly;
    private Double followerChangeMonthly;
    private Double followerRatio;
    private Double nonFollowerRatio;
    private String topPostsMediaIds;
    private String topVideosMediaIds;
    private LocalDate snapshotDate;
    private List<InstagramMediaSnapshot> mediaSnapshots;
}
