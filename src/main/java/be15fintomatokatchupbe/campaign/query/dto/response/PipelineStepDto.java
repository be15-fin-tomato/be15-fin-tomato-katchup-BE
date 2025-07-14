package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PipelineStepDto {
    private String stepType;
    private String createdAt;

}