package be15fintomatokatchupbe.user.query.mapper;

import be15fintomatokatchupbe.user.query.dto.response.UserAccountQueryResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserQueryMapper {

    /* 내 계정 정보 */
    UserAccountQueryResponse getMyAccount(Long userId);
}
