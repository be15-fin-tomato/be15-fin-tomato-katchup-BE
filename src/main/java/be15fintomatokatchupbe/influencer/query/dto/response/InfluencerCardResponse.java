package be15fintomatokatchupbe.influencer.query.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencerCardResponse {

    private Long influencerId;
    private String name;
    private String gender;
    private String manager;
    private String national;
    private Long price;
    private boolean instagramIsConnected;
    private boolean youtubeIsConnected;

    private List<CategoryDto> tags;

    private YoutubeInfoResponse youtube;
    private InstagramInfoResponse instagram;

    private String targetGender;
    private String ageRange;
}
