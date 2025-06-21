package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InfluencerRevenueRequest extends InfluencerRequest{
    private String youtubeLink;
    private String instagramLink;
}
