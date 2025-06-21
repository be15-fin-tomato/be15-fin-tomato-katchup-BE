package be15fintomatokatchupbe.campaign.command.application.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class InfluencerRequest {
    private Long influencerId;
}
