package be15fintomatokatchupbe.influencer.query.dto.response;

import be15fintomatokatchupbe.influencer.query.dto.response.CategoryDto;
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
    private String manager;

    private List<CategoryDto> tags;

    private YoutubeInfoResponse youtube;
    private InstagramInfoResponse instagram;
}
