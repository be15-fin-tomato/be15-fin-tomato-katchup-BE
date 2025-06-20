package be15fintomatokatchupbe.user.command.application.service;

import be15fintomatokatchupbe.common.domain.GenderType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangeMyAccountRequest;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangePasswordRequest;
import be15fintomatokatchupbe.user.command.application.dto.request.SignupRequest;
import be15fintomatokatchupbe.user.command.application.repository.PicFileRepository;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.PicFile;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.user.exception.UserErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCommendServiceTest {

    @Mock private ModelMapper modelMapper;
    @Mock private UserRepository userRepository;
    @Mock private PicFileRepository picFileRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCommendService userCommendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_success() {
        SignupRequest request = SignupRequest.builder()
                .email("test@example.com")
                .loginId("testuser")
                .password("password")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPwd1");

        User user = User.builder().build();
        when(modelMapper.map(request, User.class)).thenReturn(user);

        userCommendService.signup(request);

        verify(userRepository).save(user);
        assertEquals("encodedPwd1", user.getPassword());
    }

    @Test
    void changePassword_success() {
        Long userId = 1L;

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password("old")
                .newPassword("new123")
                .confirmNewPassword("new123")
                .build();

        User user = User.builder().password("encodedOld").build();

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(passwordEncoder.matches("old", "encodedOld")).thenReturn(true);
        when(passwordEncoder.encode("new123")).thenReturn("encodedNew");

        userCommendService.changePassword(userId, request);

        verify(userRepository).save(user);
        assertEquals("encodedNew", user.getPassword());
    }

    @Test
    void changeMyAccount_success() {
        Long userId = 1L;

        ChangeMyAccountRequest request = ChangeMyAccountRequest.builder()
                .name("변경된 이름")
                .phone("010-1234-5678")
                .gender(GenderType.valueOf("M"))
                .birth(null)
                .build();

        User user = mock(User.class);

        when(userRepository.findByUserId(userId)).thenReturn(user);

        userCommendService.changeMyAccount(userId, request);

        verify(user).changeMyAccount(request);
        verify(userRepository).save(user);
    }

    @Test
    void myProfileImage_insertNewFile() throws IOException {
        Long userId = 1L;
        User user = User.builder().userId(userId).fileId(null).build();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "profile.jpg", "image/jpeg", "test".getBytes());

        when(userRepository.findByUserId(userId)).thenReturn(user);

        // 실제 DB 저장은 생략
        PicFile pic = new PicFile();
        when(picFileRepository.save(any())).thenAnswer(invocation -> {
            PicFile saved = invocation.getArgument(0);
            saved.setFileId(100L);
            return saved;
        });

        userCommendService.myProfileImage(userId, mockFile);

        verify(picFileRepository).save(any(PicFile.class));
        verify(userRepository).save(user);
        assertEquals(100L, user.getFileId());
    }

    @Test
    void myProfileImage_updateExistingFile() throws IOException {
        Long userId = 1L;
        Long fileId = 123L;

        User user = User.builder().userId(userId).fileId(fileId).build();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "newprofile.png", "image/png", "bytes".getBytes());

        PicFile existing = mock(PicFile.class);

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(picFileRepository.findById(fileId)).thenReturn(Optional.of(existing));

        userCommendService.myProfileImage(userId, mockFile);

        verify(existing).profileImage(anyString(), anyString());
        verify(picFileRepository).save(existing);
    }
}
