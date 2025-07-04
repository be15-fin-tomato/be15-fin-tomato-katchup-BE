package be15fintomatokatchupbe.user.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.exception.FileErrorCode;
import be15fintomatokatchupbe.file.service.FileService;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangeMyAccountRequest;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangePasswordRequest;
import be15fintomatokatchupbe.user.command.application.dto.request.SignupRequest;
import be15fintomatokatchupbe.user.command.application.repository.PicFileRepository;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.PicFile;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.user.exception.UserErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCommendService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PicFileRepository picFileRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

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

    /* 내 계정 정보 수정 */
    @Transactional
    public void changeMyAccount(Long userId, ChangeMyAccountRequest request) {
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }
        user.changeMyAccount(request);
        userRepository.save(user);
    }

    /* 이미지 등록 및 수정 */
    @Transactional
    public void myProfileImage(Long userId, MultipartFile file) throws Exception {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }

        PicFile picFile = fileService.uploadProfileImage(file, userId);

        if (user.getFileId() == null) {
            user.setFileId(picFile.getFileId());
            userRepository.save(user);
        } else {
            PicFile existing = picFileRepository.findById(user.getFileId())
                    .orElseThrow(() -> new BusinessException(UserErrorCode.IMAGE_NOT_FOUND));
            existing.profileImage(picFile.getFileName(), picFile.getFileRoute());
            picFileRepository.save(existing);
            Long fileId = picFile.getFileId();

            user.updateFile(fileId);

        }
    }
}
