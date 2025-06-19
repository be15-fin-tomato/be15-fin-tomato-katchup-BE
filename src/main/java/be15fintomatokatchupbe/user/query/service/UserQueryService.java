package be15fintomatokatchupbe.user.query.service;

import be15fintomatokatchupbe.user.query.dto.response.UserAccountQueryResponse;
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
}
