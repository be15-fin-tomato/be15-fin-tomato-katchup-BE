package be15fintomatokatchupbe.chat.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRoomRequest {
    private Long userId;
    private List<Long> userIds;
    private String chatName;
}

