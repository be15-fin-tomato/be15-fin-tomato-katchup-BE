package be15fintomatokatchupbe.campaign.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignResultRequest {
    private Integer page = 1;
    private Integer size = 6;
    private String searchCondition;
    private String keyword;
    private String filterType;
    private String sortBy;
    private String sortOrder;
}