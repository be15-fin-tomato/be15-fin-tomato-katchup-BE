package be15fintomatokatchupbe.user.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserInfluencerListResponse {
    List<UserInfluencerListDTO> userInfluencerList;
}
