package be15fintomatokatchupbe.influencer.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InfluencerRegisterResponse {
    private Long influencerId;
    private String name;
    private boolean youtubeConnected;
    private boolean instagramConnected;
    private List<String> categoryNames;
}
