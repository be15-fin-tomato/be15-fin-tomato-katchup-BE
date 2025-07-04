package be15fintomatokatchupbe.user.query.service;

import be15fintomatokatchupbe.user.query.dto.response.UserAccountQueryResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserHeaderAccountResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserInfluencerListDTO;
import be15fintomatokatchupbe.user.query.dto.response.UserInfluencerListResponse;
import be15fintomatokatchupbe.user.query.mapper.UserQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserQueryMapper userQueryMapper;

    /* 내 계정 정보 조회 */
    public UserAccountQueryResponse getMyAccount(Long userId) {

        return userQueryMapper.getMyAccount(userId);
    }

    /* 내 인플루언서 목록 조회 */
    public UserInfluencerListResponse getMyInfluencer(Long userId) {
        List<UserInfluencerListDTO> response = userQueryMapper.getMyInfluencer(userId);
        return UserInfluencerListResponse.builder()
                .userInfluencerList(response)
                .build();
    }

    public UserHeaderAccountResponse getSimpleMyAccount(Long userId) {
        return userQueryMapper.getSimpleMyAccount(userId);
    }
}
