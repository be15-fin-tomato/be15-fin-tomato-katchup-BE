package be15fintomatokatchupbe.campaign.query.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendResult {
    @JsonProperty("best_match_influencer_ids")
    private List<Integer> bestMatchInfluencerIds;
}
