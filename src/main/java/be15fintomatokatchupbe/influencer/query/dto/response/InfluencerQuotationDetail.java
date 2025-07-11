package be15fintomatokatchupbe.influencer.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class InfluencerQuotationDetail {
    private Long influencerId;
    private String name;
    private String youtubeName;
    private String imageUrl;
    private String instagramName;

    // YouTube 관련 필드
    private Long subscriber;
    private Double youtubeAvgViews;
    private Double youtubeAvgLikes;
    private Double youtubeAvgComments;
    private Double youtubeAge1317;
    private Double youtubeAge1824;
    private Double youtubeAge2534;
    private Double youtubeAge3544;
    private Double youtubeAge4554;
    private Double youtubeAge5564;
    private Double youtubeAge65Plus;
    private Double youtubeGenderFemale;
    private Double youtubeGenderMale;

    // Instagram 관련 필드
    private Long follower;
    private Double instagramAvgViews;
    private Double instagramAvgLikes;
    private Double instagramAvgComments;
    private Double instagramAge1317;
    private Double instagramAge1824;
    private Double instagramAge2534;
    private Double instagramAge3544;
    private Double instagramAge4554;
    private Double instagramAge5564;
    private Double instagramAge65Plus;
    private Double instagramGenderFemale;
    private Double instagramGenderMale;

    private List<CampaignRecord> campaignRecord;
}
