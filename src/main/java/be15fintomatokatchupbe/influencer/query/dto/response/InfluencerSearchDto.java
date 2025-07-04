package be15fintomatokatchupbe.influencer.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InfluencerSearchDto {
    public Long id;
    public String name;
}
