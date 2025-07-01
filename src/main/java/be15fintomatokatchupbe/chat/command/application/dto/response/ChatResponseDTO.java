package be15fintomatokatchupbe.chat.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ChatResponseDTO {

    private String fcmToken;

    private Long userId;

    public Long userId() {
        return userId;
    }

    public String fcmToken() {
        return fcmToken;
    }
}
