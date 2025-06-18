package be15fintomatokatchupbe.auth.command.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.common.exception.GlobalErrorCode;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.infra.redis.RefreshTokenRepository;
import be15fintomatokatchupbe.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthCommandService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public void logout(CustomUserDetail userDetail, String refreshToken) {
        /* 0. 토큰 검증 */
        jwtTokenProvider.validateToken(refreshToken);

        /* 1. 리프레쉬 토큰에서 값 추출 */
        Long userId = jwtTokenProvider.getUserIdFromJWT(refreshToken);

        /* 2. 유저 정보 확인 */
        /* 예외 상황, accessToken의 memberId와 refreshToken의 memberId가 다른 경우! */
        if(!Objects.equals(userDetail.getUserId(), userId)){
            throw new BusinessException(GlobalErrorCode.UNAUTHORIZED_REQUEST);
        }

        /* 3. Redis에 있는 리프레쉬 토큰 제거  */
        refreshTokenRepository.delete(userId);
    }

}
