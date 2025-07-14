package be15fintomatokatchupbe.auth.command.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.common.exception.GlobalErrorCode;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.infra.redis.RefreshTokenRepository;
import be15fintomatokatchupbe.utils.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthCommandServiceTest {

    private JwtTokenProvider jwtTokenProvider;
    private RefreshTokenRepository refreshTokenRepository;
    private AuthCommandService authCommandService;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        authCommandService = new AuthCommandService(jwtTokenProvider, refreshTokenRepository);
    }

    @Test
    void logout_success() {
        // given
        Long userId = 123L;
        String refreshToken = "fake-refresh-token";
        CustomUserDetail userDetail = mock(CustomUserDetail.class);
        when(userDetail.getUserId()).thenReturn(userId);

        when(jwtTokenProvider.getUserIdFromJWT(refreshToken)).thenReturn(userId);

        // when
        assertDoesNotThrow(() -> authCommandService.logout(userDetail, refreshToken));

        // then
        verify(jwtTokenProvider).validateToken(refreshToken);
        verify(jwtTokenProvider).getUserIdFromJWT(refreshToken);
        verify(refreshTokenRepository).delete(userId);
    }

    @Test
    void logout_invalidUserId_throwsException() {
        // given
        Long accessUserId = 123L;
        Long refreshUserId = 456L;
        String refreshToken = "fake-refresh-token";
        CustomUserDetail userDetail = mock(CustomUserDetail.class);
        when(userDetail.getUserId()).thenReturn(accessUserId);

        when(jwtTokenProvider.getUserIdFromJWT(refreshToken)).thenReturn(refreshUserId);

        // when & then
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> authCommandService.logout(userDetail, refreshToken)
        );

        assertEquals(GlobalErrorCode.UNAUTHORIZED_REQUEST, ex.getErrorCode());
        verify(jwtTokenProvider).validateToken(refreshToken);
        verify(jwtTokenProvider).getUserIdFromJWT(refreshToken);
        verify(refreshTokenRepository, never()).delete(anyLong());
    }
}
