package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignResponse {
    private String clientCompanyName;
    private String campaignName;
    private String productName;
    private String youtubeLink;
    private String instagramLink;
}
