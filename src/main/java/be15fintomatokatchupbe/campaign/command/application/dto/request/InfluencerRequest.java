package be15fintomatokatchupbe.campaign.command.application.dto.request;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InfluencerRequest {
    private Long influencerId;
}
