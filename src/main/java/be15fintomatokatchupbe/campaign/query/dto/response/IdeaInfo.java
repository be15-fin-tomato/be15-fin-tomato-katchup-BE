package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IdeaInfo {

    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private String content;

}