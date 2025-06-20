package be15fintomatokatchupbe.user.command.application.support;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.user.exception.UserErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserHelperService {
    private final UserRepository userRepository;

    public User findValidUser(Long id){
        return userRepository.findByUserIdAndIsDeleted(id, StatusType.N)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    // 유저 목록으로 찾기
    public List<User> findValidUserList(List<Long> userId){
        return userRepository.findByUserIdInAndIsDeleted(userId, StatusType.N);
    }
}
