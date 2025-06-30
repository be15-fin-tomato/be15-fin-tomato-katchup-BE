package be15fintomatokatchupbe.chat.query.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/* Mongo에서 조회 */

@Getter
@Setter
public class LastMessageDto {
    private Long chatId;
    private String message;
    private LocalDateTime sentAt;
}
