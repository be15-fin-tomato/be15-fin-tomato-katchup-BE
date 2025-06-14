package be15fintomatokatchupbe.user.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
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
}
