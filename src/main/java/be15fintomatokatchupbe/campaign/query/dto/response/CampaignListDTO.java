package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CampaignListDTO {
    private Long campaignId;
    private String campaignName;

    private Long campaignStatusId;
    private String campaignStatusName;

    private String clientCompanyName;
    private Long clientCompanyId;

    private String clientManagerName;
    private String clientManagerPosition;
    private Long expectedRevenue;
    private String productName;

    private String startedAt;
    private String endedAt;

    private String pipelineUserName;
    private String pipelineUserPosition;

    private int successProbability;

    private List<PipelineStepDto> pipelineSteps = new ArrayList<>();

}
