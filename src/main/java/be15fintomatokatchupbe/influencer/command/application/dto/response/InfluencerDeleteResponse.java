package be15fintomatokatchupbe.influencer.command.application.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InfluencerDeleteResponse {
    private Long influencerId;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    private boolean instagramIsConnected;
    private boolean youtubeIsConnected;
    private String message;
}
