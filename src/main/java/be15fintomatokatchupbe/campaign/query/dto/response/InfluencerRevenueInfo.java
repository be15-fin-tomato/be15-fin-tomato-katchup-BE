package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class InfluencerRevenueInfo {
    private Long influencerId;
    private String influencerName;
    private String youtubeLink;
    private String instagramLink;
    private Long adPrice;
}
