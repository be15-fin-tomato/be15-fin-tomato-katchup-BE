package be15fintomatokatchupbe.chat.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExitChatRoomRequest {
    private Long userId;
    private Long chatId;
}
