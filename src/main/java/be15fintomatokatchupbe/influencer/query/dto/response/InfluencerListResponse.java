package be15fintomatokatchupbe.influencer.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencerListResponse {
    private List<InfluencerCardResponse> data;
    private Pagination pagination;
}
