package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CampaignListsDTO {
    private Long campaignId;
    private String campaignName;
    private Long campaignStatusId;
    private String campaignStatusName;
    private String clientCompanyName;
    private String clientManagerName;
    private String clientManagerPosition;
    private Long expectedRevenue;
    private String productName;
    private String startedAt;
    private String endedAt;

    private String pipelineUserName;
    private String pipelineUserPosition;

    private int successProbability;

    private LocalDateTime firstCreatedAt;

    private List<PipelineStepsDto> pipelineSteps;

}
