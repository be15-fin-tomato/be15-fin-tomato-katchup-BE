package be15fintomatokatchupbe.campaign.query.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfluencerInfo {
    private Long influencerId;
    private String name;
    private Youtube youtube;
    private Instagram instagram;

    @Getter
    @Setter
    private static class Youtube{
        private String thumbnailUrl;
        private String name;
    }

    @Getter
    @Setter
    private static class Instagram{
        private String name;
    }
}
