package be15fintomatokatchupbe.user.query.mapper;

import be15fintomatokatchupbe.user.query.dto.response.UserAccountQueryResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserInfluencerListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserQueryMapper {

    /* 내 계정 정보 */
    UserAccountQueryResponse getMyAccount(Long userId);

    /* 내 인플루언서 목록 조회 */
    List<UserInfluencerListDTO> getMyInfluencer(Long userId);
}
