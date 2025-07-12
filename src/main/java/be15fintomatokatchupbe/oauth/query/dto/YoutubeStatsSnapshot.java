package be15fintomatokatchupbe.oauth.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class YoutubeStatsSnapshot {
    private int totalVideos;
    private double avgViews;
    private double avgLikes;
    private double avgComments;
    private double dailyAvgViews;
    private double monthlyAvgViews;

    // 연령대
    private double age1317;
    private double age1824;
    private double age2534;
    private double age3544;
    private double age4554;
    private double age5564;
    private double age65Plus;

    // 성별
    private double genderMale;
    private double genderFemale;

    // 구독자 변화량
    private int subscriberChangeDaily;
    private int subscriberChangeWeekly;
    private int subscriberChangeMonthly;

    // 구독자/비구독자 시청 비율
    private double subscribedRatio;
    private double notSubscribedRatio;
}
