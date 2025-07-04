package be15fintomatokatchupbe.user.query.mapper;

import be15fintomatokatchupbe.chat.query.application.dto.response.UserSimpleDto;
import be15fintomatokatchupbe.user.query.dto.response.UserAccountQueryResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserInfluencerListDTO;
import be15fintomatokatchupbe.user.query.dto.response.UserSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserQueryMapper {

    /* 내 계정 정보 */
    UserAccountQueryResponse getMyAccount(Long userId);

    /* 내 인플루언서 목록 조회 */
    List<UserInfluencerListDTO> getMyInfluencer(Long userId);

    List<UserSimpleDto> findUserNamesByIds(Set<Long> senderIds);

    List<Long> findUserIdsByIds(@Param("userIds") List<Long> userIds);

    List<UserSearchDto> getUserList(String keyword);
}
