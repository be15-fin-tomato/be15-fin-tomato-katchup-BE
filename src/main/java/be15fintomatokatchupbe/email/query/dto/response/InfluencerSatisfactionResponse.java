package be15fintomatokatchupbe.email.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfluencerSatisfactionResponse {
    private Double satisfaction;
    private int count;
}
