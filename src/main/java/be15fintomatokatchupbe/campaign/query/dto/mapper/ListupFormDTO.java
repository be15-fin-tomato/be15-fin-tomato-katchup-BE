package be15fintomatokatchupbe.campaign.query.dto.mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListupFormDTO {
    private Long campaignId;
    private String campaignName;
    private Long clientCompanyId;
    private String clientCompanyName;
}
