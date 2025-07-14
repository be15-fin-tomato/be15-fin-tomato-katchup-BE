package be15fintomatokatchupbe.campaign.query.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfluencerInfo {
    private Long influencerId;
    private String name;
    private Youtube youtube;
    private Instagram instagram;
    private List<String> categoryList;

    @Getter
    @Setter
    private static class Youtube{
        private String thumbnailUrl;
        private Long subscriber;
        private String name;
    }

    @Getter
    @Setter
    private static class Instagram{
        private String name;
        private Long follower;
    }
}
