package be15fintomatokatchupbe.chat.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateChatRoomResponse {
    private Long chatId;
    private String message;
}
