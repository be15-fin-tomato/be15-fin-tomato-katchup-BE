package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CampaignContentThumbnail {
    private String channelId;
    private String channelThumbnail;
}
