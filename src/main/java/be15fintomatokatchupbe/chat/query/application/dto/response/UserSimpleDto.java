package be15fintomatokatchupbe.chat.query.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSimpleDto {
    private Long userId;
    private String userName;
}

