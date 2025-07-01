package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class IdeaDetailResponse {

    private final Long ideaId;
    private final Long pipelineId;
    private final Long userId;
    private final String name;
    private final String content;
    private final LocalDateTime createdAt;

}