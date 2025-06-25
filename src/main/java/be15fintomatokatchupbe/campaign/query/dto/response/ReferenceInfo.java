package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ReferenceInfo {
    private final Long pipelineId;
    private final String name;
}
