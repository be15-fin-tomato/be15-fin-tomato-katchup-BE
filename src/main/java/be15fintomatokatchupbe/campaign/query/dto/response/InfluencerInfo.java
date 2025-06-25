package be15fintomatokatchupbe.campaign.query.dto.response;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class InfluencerInfo {
    private final Long influencerId;
    private final String influencerName;
}
