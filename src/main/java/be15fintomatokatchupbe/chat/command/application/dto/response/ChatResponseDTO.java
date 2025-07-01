package be15fintomatokatchupbe.chat.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ChatResponseDTO {
    String fcmToken;
    Long userId;

}
