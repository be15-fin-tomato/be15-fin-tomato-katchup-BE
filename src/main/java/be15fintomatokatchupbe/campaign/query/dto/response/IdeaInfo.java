package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public class IdeaInfo {
    private final Long userId;
    private final String userName;
    private final LocalDateTime createdAt;
    private final String content;
}
