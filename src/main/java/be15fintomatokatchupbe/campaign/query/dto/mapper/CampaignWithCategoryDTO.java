package be15fintomatokatchupbe.campaign.query.dto.mapper;

import be15fintomatokatchupbe.influencer.query.dto.response.CategoryDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CampaignWithCategoryDTO {
    private Long clientCompanyId;
    private String clientCompanyName;
    private Long campaignId;
    private String campaignName;
    private Long campaignStatusId;
    private String productName;
    private List<CategoryDto> categoryList;
}
