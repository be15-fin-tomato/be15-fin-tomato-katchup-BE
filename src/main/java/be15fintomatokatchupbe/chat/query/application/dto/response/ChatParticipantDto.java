package be15fintomatokatchupbe.chat.query.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatParticipantDto {
    private Long userId;
    private String name;
}
