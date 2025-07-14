package be15fintomatokatchupbe.campaign.query.dto.mapper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestCampaign {
    private Long campaignId;
    private String productName;
    private List<Integer> categoryList;
}
