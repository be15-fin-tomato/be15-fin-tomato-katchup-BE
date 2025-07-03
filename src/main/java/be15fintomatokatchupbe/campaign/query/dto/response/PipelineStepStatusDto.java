package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineStepStatusDto {
    private Long campaignId;
    private String stepType;
    private String startedAt;
}