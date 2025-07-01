package be15fintomatokatchupbe.chat.query.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatRoomDetailResponse {
    private Long chatId;
    private List<MessageResponse> messages;
}

