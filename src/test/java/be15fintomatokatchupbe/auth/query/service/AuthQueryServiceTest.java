package be15fintomatokatchupbe.auth.query.service;

import be15fintomatokatchupbe.auth.command.dto.response.TokenResponse;
import be15fintomatokatchupbe.auth.query.dto.request.LoginRequest;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.common.exception.GlobalErrorCode;
import be15fintomatokatchupbe.infra.redis.RefreshTokenRepository;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.user.exception.UserErrorCode;
import be15fintomatokatchupbe.utils.EmailUtils;
import be15fintomatokatchupbe.utils.RandomStringUtils;
import be15fintomatokatchupbe.utils.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthQueryServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private RefreshTokenRepository refreshTokenRepository;
    private RandomStringUtils randomStringUtils;
    private EmailUtils emailUtils;
    private AuthQueryService authQueryService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        randomStringUtils = mock(RandomStringUtils.class);
        emailUtils = mock(EmailUtils.class);

        authQueryService = new AuthQueryService(
                userRepository,
                passwordEncoder,
                jwtTokenProvider,
                refreshTokenRepository,
                randomStringUtils,
                emailUtils
        );
    }

    @Test
    void login_success() {
        // given
        String loginId = "user123";
        String password = "password";
        LoginRequest request = new LoginRequest(loginId, password);
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(1L);
        when(user.getName()).thenReturn("홍길동");
        when(user.getLoginId()).thenReturn(loginId);
        when(user.getPassword()).thenReturn("encodedPw");

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPw")).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(anyLong(), anyString(), anyString())).thenReturn("access-token");
        when(jwtTokenProvider.createRefreshToken(anyLong(), anyString(), anyString())).thenReturn("refresh-token");

        // when
        TokenResponse response = authQueryService.login(request);

        // then
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(refreshTokenRepository).save(eq(1L), eq("refresh-token"));
    }

    @Test
    void login_invalidLoginId_throwsException() {
        when(userRepository.findByLoginId("wrongId")).thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest("wrongId", "pw");

        BusinessException ex = assertThrows(BusinessException.class, () -> authQueryService.login(request));
        assertEquals(UserErrorCode.INVALID_LOGIN_ID_REQUEST, ex.getErrorCode());
    }

    @Test
    void login_invalidPassword_throwsException() {
        String loginId = "user123";
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("encodedPw");

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPw", "encodedPw")).thenReturn(false);

        LoginRequest request = new LoginRequest(loginId, "wrongPw");

        BusinessException ex = assertThrows(BusinessException.class, () -> authQueryService.login(request));
        assertEquals(UserErrorCode.INVALID_PASSWORD_REQUEST, ex.getErrorCode());
    }

    @Test
    void reissue_success() {
        String oldToken = "refresh-token";
        Long userId = 1L;
        String name = "홍길동";
        String loginId = "user123";

        when(jwtTokenProvider.validateToken(oldToken)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromJWT(oldToken)).thenReturn(userId);
        when(jwtTokenProvider.getLoginIdFromJWT(oldToken)).thenReturn(loginId);
        when(jwtTokenProvider.getUserNameFromJWT(oldToken)).thenReturn(name);
        when(refreshTokenRepository.find(userId)).thenReturn(oldToken);
        when(jwtTokenProvider.createAccessToken(userId, name, loginId)).thenReturn("new-access");
        when(jwtTokenProvider.createRefreshToken(userId, name, loginId)).thenReturn("new-refresh");

        TokenResponse response = authQueryService.reissue(oldToken);

        assertEquals("new-access", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());
        verify(refreshTokenRepository).save(userId, "new-refresh");
    }

    @Test
    void reissue_expiredToken_throwsException() {
        Long userId = 1L;
        String token = "expired-token";

        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(refreshTokenRepository.find(userId)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> authQueryService.reissue(token));
        assertEquals(GlobalErrorCode.REFRESH_TOKEN_EXPIRED, ex.getErrorCode());
    }

    @Test
    void findPassword_success() {
        String loginId = "user123";
        String email = "user@example.com";
        User user = mock(User.class);
        String tempPw = "TEMP1234";
        String encoded = "encodedTEMP1234";

        when(userRepository.existsByLoginId(loginId)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(randomStringUtils.getRandomString(20)).thenReturn(tempPw);
        when(passwordEncoder.encode(tempPw)).thenReturn(encoded);

        doNothing().when(user).update(encoded);
        doNothing().when(emailUtils).sendEmail(anyString(), anyString(), eq(email));

        authQueryService.findPassword(loginId, email);

        verify(user).update(encoded);
        verify(emailUtils).sendEmail(contains(tempPw), anyString(), eq(email));
    }

    @Test
    void findPassword_loginIdNotFound_throwsException() {
        when(userRepository.existsByLoginId("missing")).thenReturn(false);
        BusinessException ex = assertThrows(BusinessException.class, () -> authQueryService.findPassword("missing", "email@example.com"));
        assertEquals(UserErrorCode.USER_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void findPassword_emailNotFound_throwsException() {
        when(userRepository.existsByLoginId("user")).thenReturn(true);
        when(userRepository.findByEmail("missing@example.com")).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> authQueryService.findPassword("user", "missing@example.com"));
        assertEquals(UserErrorCode.EMAIL_NOT_FOUND, ex.getErrorCode());
    }
}
