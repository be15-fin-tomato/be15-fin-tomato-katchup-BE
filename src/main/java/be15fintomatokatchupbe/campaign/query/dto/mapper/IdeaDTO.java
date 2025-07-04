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
    private Long userId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;

}
