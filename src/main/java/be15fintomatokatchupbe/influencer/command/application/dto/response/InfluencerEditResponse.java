package be15fintomatokatchupbe.influencer.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InfluencerEditResponse {
    private Long influencerId;
    private String gender;
    private Long price;
    private List<String> categoryNames;
}

