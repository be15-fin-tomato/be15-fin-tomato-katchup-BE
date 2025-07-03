package be15fintomatokatchupbe.chat.command.application.dto.request;

import be15fintomatokatchupbe.chat.query.application.dto.response.UserSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatInviteRequest {
    private Long userId;
    private List<Long> invitedUserIds;
}

