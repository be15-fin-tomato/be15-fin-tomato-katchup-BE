package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PipelineStepsDto {
    private Long campaignId;
    private Long pipelineStatusId;
    private String stepType;
    private String createdAt;

}