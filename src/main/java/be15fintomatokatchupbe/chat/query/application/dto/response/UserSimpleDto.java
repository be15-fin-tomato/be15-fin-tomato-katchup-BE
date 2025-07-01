package be15fintomatokatchupbe.chat.query.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSimpleDto {
    private Long userId;
    private String userName;
}

