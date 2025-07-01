package be15fintomatokatchupbe.chat.query.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {
    private Long chatId;
    private String lastMessage;
    private LocalDateTime lastSentAt;
    private List<ChatParticipantDto> participants;
    private Long unreadCount;
}


