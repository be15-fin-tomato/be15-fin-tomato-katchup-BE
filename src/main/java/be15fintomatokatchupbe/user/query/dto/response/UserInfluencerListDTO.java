package be15fintomatokatchupbe.user.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfluencerListDTO {

    private Long influencerId;

    private Long userId;

    private String name;

    private String imageUrl;

}
