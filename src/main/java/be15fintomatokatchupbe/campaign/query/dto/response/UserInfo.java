package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserInfo {
    private Long userId;
    private String userName;
}
