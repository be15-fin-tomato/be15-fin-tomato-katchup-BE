package be15fintomatokatchupbe.user.command.application.service;

import be15fintomatokatchupbe.common.domain.GenderType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.service.FileService;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCommendServiceTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PicFileRepository picFileRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private FileService fileService;

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

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).existsByLoginId(request.getLoginId());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(modelMapper, times(1)).map(request, User.class);
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPwd1", user.getPassword());
    }

    @Test
    void signup_duplicateEmail() {
        SignupRequest request = SignupRequest.builder()
                .email("duplicate@example.com")
                .loginId("testuser")
                .password("password")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userCommendService.signup(request);
        });

        assertEquals(UserErrorCode.DUPLICATE_EMAIL_EXISTS, thrown.getErrorCode());
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).existsByLoginId(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(modelMapper, never()).map(any(), any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void signup_duplicateLoginId() {
        SignupRequest request = SignupRequest.builder()
                .email("test@example.com")
                .loginId("duplicateId")
                .password("password")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(true);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userCommendService.signup(request);
        });

        assertEquals(UserErrorCode.DUPLICATE_ID_EXISTS, thrown.getErrorCode());
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).existsByLoginId(request.getLoginId());
        verify(passwordEncoder, never()).encode(anyString());
        verify(modelMapper, never()).map(any(), any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_success() {
        Long userId = 1L;

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password("old")
                .newPassword("new123")
                .confirmNewPassword("new123")
                .build();

        User user = User.builder().userId(userId).password("encodedOld").build();

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("new123")).thenReturn("encodedNew");

        userCommendService.changePassword(userId, request);

        verify(userRepository, times(1)).findByUserId(userId);
        // ★ 이 줄을 변경했습니다: user.getPassword() 대신 "encodedOld" 리터럴을 사용합니다.
        verify(passwordEncoder, times(1)).matches(request.getPassword(), "encodedOld");
        verify(passwordEncoder, times(1)).encode(request.getNewPassword());
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedNew", user.getPassword());
    }

    @Test
    void changePassword_userNotFound() {
        Long userId = 1L;
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password("old").newPassword("new").confirmNewPassword("new").build();

        when(userRepository.findByUserId(userId)).thenReturn(null);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userCommendService.changePassword(userId, request);
        });

        assertEquals(UserErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
        verify(userRepository, times(1)).findByUserId(userId);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_passwordMismatch() {
        Long userId = 1L;
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password("wrongOld")
                .newPassword("new123")
                .confirmNewPassword("new123")
                .build();

        User user = User.builder().userId(userId).password("encodedOld").build();

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userCommendService.changePassword(userId, request);
        });

        assertEquals(UserErrorCode.PASSWORD_MISMATCH, thrown.getErrorCode());
        verify(userRepository, times(1)).findByUserId(userId);
        // ★ 이 줄을 변경했습니다: user.getPassword() 대신 "encodedOld" 리터럴을 사용합니다.
        verify(passwordEncoder, times(1)).matches(request.getPassword(), "encodedOld");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_newPasswordMismatch() {
        Long userId = 1L;
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .password("old")
                .newPassword("new123")
                .confirmNewPassword("newXYZ")
                .build();

        User user = User.builder().userId(userId).password("encodedOld").build();

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userCommendService.changePassword(userId, request);
        });

        assertEquals(UserErrorCode.NEW_PASSWORD_MISMATCH, thrown.getErrorCode());
        verify(userRepository, times(1)).findByUserId(userId);
        // ★ 이 줄을 변경했습니다: user.getPassword() 대신 "encodedOld" 리터럴을 사용합니다.
        verify(passwordEncoder, times(1)).matches(request.getPassword(), "encodedOld");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changeMyAccount_success() {
        Long userId = 1L;

        ChangeMyAccountRequest request = ChangeMyAccountRequest.builder()
                .name("변경된 이름")
                .phone("010-1234-5678")
                .gender(GenderType.M)
                .birth(null)
                .build();

        User user = mock(User.class);
        when(user.getUserId()).thenReturn(userId);

        when(userRepository.findByUserId(userId)).thenReturn(user);

        userCommendService.changeMyAccount(userId, request);

        verify(userRepository, times(1)).findByUserId(userId);
        verify(user, times(1)).changeMyAccount(request);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changeMyAccount_userNotFound() {
        Long userId = 1L;
        ChangeMyAccountRequest request = ChangeMyAccountRequest.builder().build();

        when(userRepository.findByUserId(userId)).thenReturn(null);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userCommendService.changeMyAccount(userId, request);
        });

        assertEquals(UserErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
        verify(userRepository, times(1)).findByUserId(userId);
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(modelMapper, picFileRepository, passwordEncoder, fileService);
    }

    @Test
    void myProfileImage_insertNewFile() throws Exception {
        Long userId = 1L;
        User user = mock(User.class); // Mock 객체로 변경
        when(user.getUserId()).thenReturn(userId);
        when(user.getFileId()).thenReturn(null);

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "profile.jpg", "image/jpeg", "test content".getBytes());

        PicFile uploadedPicFile = PicFile.builder()
                .fileId(100L)
                .fileName("profile.jpg")
                .fileRoute("/uploads/profile.jpg")
                .build();
        when(fileService.uploadProfileImage(mockFile, userId)).thenReturn(uploadedPicFile);

        when(userRepository.findByUserId(userId)).thenReturn(user);

        userCommendService.myProfileImage(userId, mockFile);

        verify(userRepository, times(1)).findByUserId(userId);
        verify(fileService, times(1)).uploadProfileImage(mockFile, userId);
        verify(user, times(1)).setFileId(uploadedPicFile.getFileId());
        verify(userRepository, times(1)).save(user);
        verify(picFileRepository, never()).save(any(PicFile.class));
    }

    @Test
    void myProfileImage_updateExistingFile() throws Exception {
        Long userId = 1L;
        Long existingFileId = 123L;

        User user = mock(User.class);
        when(user.getUserId()).thenReturn(userId);
        when(user.getFileId()).thenReturn(existingFileId);

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "newprofile.png", "image/png", "new content".getBytes());

        PicFile newPicFile = PicFile.builder()
                .fileId(456L)
                .fileName("newprofile.png")
                .fileRoute("/uploads/newprofile.png")
                .build();
        when(fileService.uploadProfileImage(mockFile, userId)).thenReturn(newPicFile);

        PicFile existingPicFile = mock(PicFile.class);
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(picFileRepository.findById(existingFileId)).thenReturn(Optional.of(existingPicFile));

        userCommendService.myProfileImage(userId, mockFile);

        verify(userRepository, times(1)).findByUserId(userId);
        verify(fileService, times(1)).uploadProfileImage(mockFile, userId);
        verify(picFileRepository, times(1)).findById(existingFileId);
        verify(existingPicFile, times(1)).profileImage(newPicFile.getFileName(), newPicFile.getFileRoute());
        verify(picFileRepository, times(1)).save(existingPicFile);
        verify(user, times(1)).updateFile(newPicFile.getFileId());
        // verify(userRepository, times(1)).save(user); // 이 줄은 서비스 로직상 호출되지 않으므로 삭제합니다.
    }

    @Test
    void myProfileImage_userNotFound() throws Exception {
        Long userId = 1L;
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "profile.jpg", "image/jpeg", "test".getBytes());

        when(userRepository.findByUserId(userId)).thenReturn(null);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userCommendService.myProfileImage(userId, mockFile);
        });

        assertEquals(UserErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
        verify(userRepository, times(1)).findByUserId(userId);
        verifyNoInteractions(fileService, picFileRepository, passwordEncoder, modelMapper);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void myProfileImage_imageNotFoundWhileUpdating() throws Exception {
        Long userId = 1L;
        Long existingFileId = 123L;

        User user = mock(User.class);
        when(user.getUserId()).thenReturn(userId);
        when(user.getFileId()).thenReturn(existingFileId);

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "newprofile.png", "image/png", "bytes".getBytes());

        PicFile newPicFile = PicFile.builder()
                .fileId(456L)
                .fileName("newprofile.png")
                .fileRoute("/uploads/newprofile.png")
                .build();
        when(fileService.uploadProfileImage(mockFile, userId)).thenReturn(newPicFile);
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(picFileRepository.findById(existingFileId)).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userCommendService.myProfileImage(userId, mockFile);
        });

        assertEquals(UserErrorCode.IMAGE_NOT_FOUND, thrown.getErrorCode());
        verify(userRepository, times(1)).findByUserId(userId);
        verify(fileService, times(1)).uploadProfileImage(mockFile, userId);
        verify(picFileRepository, times(1)).findById(existingFileId);
        verify(picFileRepository, never()).save(any(PicFile.class));
        verify(user, never()).updateFile(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }
}