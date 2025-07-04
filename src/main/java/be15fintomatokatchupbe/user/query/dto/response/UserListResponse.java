package be15fintomatokatchupbe.user.query.dto.response;

import be15fintomatokatchupbe.chat.query.application.dto.response.UserSimpleDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserListResponse {
    public List<UserSimpleDto> userList;
}
