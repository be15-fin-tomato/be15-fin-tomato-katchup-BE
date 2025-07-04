package be15fintomatokatchupbe.user.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSearchDto {
    public Long id;
    public String name;
}
