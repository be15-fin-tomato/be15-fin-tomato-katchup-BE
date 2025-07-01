package be15fintomatokatchupbe.campaign.query.dto.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class IdeaDTO {

    private Long ideaId;
    private Long pipelineId;
    private Long userId;
    private String name;
    private String content;
    private LocalDateTime createdAt;

}