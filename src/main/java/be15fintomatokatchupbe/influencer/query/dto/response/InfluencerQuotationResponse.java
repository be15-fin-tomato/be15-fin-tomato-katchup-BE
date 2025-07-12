package be15fintomatokatchupbe.influencer.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InfluencerQuotationResponse {
    List<InfluencerQuotationDetail> influencerDetail;
}
