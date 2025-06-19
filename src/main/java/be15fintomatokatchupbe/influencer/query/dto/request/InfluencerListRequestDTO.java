package be15fintomatokatchupbe.influencer.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfluencerListRequestDTO {
    private Integer page = 0;   // 기본 0페이지
    private Integer size = 6;   // 기본 6개씩
}
