package be15fintomatokatchupbe.chat.query.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {
    private Long senderId;
    private String senderName;
    private String message;
    private LocalDateTime sentAt;
    private boolean isMine;
}
