package be15fintomatokatchupbe.user.command.application.service;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangePasswordRequest;
import be15fintomatokatchupbe.user.command.application.dto.request.SignupRequest;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.user.exception.UserErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommendService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(@Valid SignupRequest request) {

        /* 이미 존재하는 이메일 */
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(UserErrorCode.DUPLICATE_EMAIL_EXISTS);
        }
        /* 이미 존재하는 회원아이디 */
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new BusinessException(UserErrorCode.DUPLICATE_ID_EXISTS);
        }

        String encryptPassword = passwordEncoder.encode(request.getPassword());

        User newUser = modelMapper.map(request, User.class);
        newUser.setPassword(encryptPassword);

        userRepository.save(newUser);
    }

    /* 비밀번호 변경 */
    public void changePassword(Long userId, @Valid ChangePasswordRequest request) {

        User user = userRepository.findByUserId(userId);
        /* 회원이 존재하는지 확인 */
        if(user  == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }
        /* 현재 비밀번호와 일치하지 않는 경우 */
        if(passwordEncoder.matches(user.getPassword(), request.getPassword())) {
            throw new BusinessException(UserErrorCode.PASSWORD_MISMATCH);
        }
        /* 새로운 비밀번호가 일치하지않을 때*/
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BusinessException(UserErrorCode.NEW_PASSWORD_MISMATCH);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    /* 참조용 */
    
    // 삭제되지 않은 유저 찾기
    public User findValidUser(Long id){
        return userRepository.findByUserIdAndIsDeleted(id, StatusType.N)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }
}
