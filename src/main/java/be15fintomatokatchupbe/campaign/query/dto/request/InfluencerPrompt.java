package be15fintomatokatchupbe.campaign.query.dto.request;

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
public class InfluencerPrompt {
    @JsonProperty("advertisement_product_name")
    private String advertisementProductName;

    private List<InfluencerEntry> influencer;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfluencerEntry {
        private Integer id;
        private List<String> product;
    }
}
