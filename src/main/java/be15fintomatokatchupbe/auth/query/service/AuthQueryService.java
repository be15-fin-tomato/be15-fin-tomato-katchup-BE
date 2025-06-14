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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthQueryService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RandomStringUtils randomStringUtils;
    private final EmailUtils emailUtil;

    /* 로그인 */
    public TokenResponse login(LoginRequest request) {

        /* 1. DB에서 유저 정보 존재 확인 */
        User foundUser = (User) userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new BusinessException(UserErrorCode.INVALID_LOGIN_ID_REQUEST));

        /* 2. 유저 정보 일치 확인 */
        if (!passwordEncoder.matches(request.getPassword(), foundUser.getPassword())) {
            throw new BusinessException(UserErrorCode.INVALID_PASSWORD_REQUEST);
        }

        /* 로그인 성공 */

        /* 3. access token 발급 */
        String accessToken = jwtTokenProvider.createAccessToken(foundUser.getUserId(), foundUser.getLoginId());

        // 4. refresh 토큰 발급
        String refreshToken = jwtTokenProvider.createRefreshToken(foundUser.getUserId(), foundUser.getLoginId());


        /* 5. redis에 토큰 저장 */
        refreshTokenRepository.save(foundUser.getUserId(), refreshToken);

        /* 6. 사용자에게 토큰 전달 */
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse reissue(String refreshToken) {

        /* 1. 토큰 검증 */
        jwtTokenProvider.validateToken(refreshToken);

        Long requestUserId = jwtTokenProvider.getUserIdFromJWT(refreshToken);
        String requestLoginId = jwtTokenProvider.getLoginIdFromJWT(refreshToken);


        log.info("재발행 요청 유저 ID : " + requestUserId);
        log.info("재발행 요청 유저 사원코드 : " + requestLoginId);

        /* 2. Redis에서 토큰 존재 확인 */
        String storedRefreshToken = refreshTokenRepository.find(requestUserId);

        /* Redis에서 찾지 못한경우 에러 응답 */
        if(storedRefreshToken == null){
            throw new BusinessException(GlobalErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(requestUserId, requestLoginId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(requestUserId, requestLoginId);

        refreshTokenRepository.save(requestUserId, newRefreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional
    public void findPassword(String loginId, String email) {

        // 1. 사원코드가 DB에 존재하는 유저인지 조회
        boolean loginExists = userRepository.existsByLoginId(loginId);
        if(!loginExists){
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }

        // 2. 이메일이 DB에 존재하는 유저인지 조회
        User user = (User) userRepository.findByEmail(email);
        if(user == null){
            throw new BusinessException(UserErrorCode.EMAIL_NOT_FOUND);
        }

        // 랜덤 문자열 생성
        String randomString = randomStringUtils.getRandomString(20);
        String encryptPassword = passwordEncoder.encode(randomString);

        // DB 업데이트
        user.update(encryptPassword);

        // 3. 메일 전송 폼 작성
        String title = "[Katchup] 비밀번호를 잊으셨나요? 임시비밀번호를 발급해드릴게요.";

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='max-width: 480px; margin: 0 auto; padding: 30px; font-family: Arial, sans-serif; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;'>")
                .append("<h2 style='color: #333; text-align: center;'>임시 비밀번호 안내</h2>")
                .append("<p style='font-size: 14px; color: #555; text-align: center;'>아래의 임시 비밀번호를 복사하여 로그인해 주세요.</p>")
                .append("<div style='margin: 20px auto; padding: 15px; background-color: #f0f8ff; border: 1px dashed #1a73e8; border-radius: 5px; text-align: center;'>")
                .append("<code style='font-size: 18px; font-weight: bold; color: #1a73e8; user-select: all;'>")
                .append(randomString)
                .append("</code>")
                .append("</div>");

        String content = sb.toString();

        // 4. 이메일 전송
        emailUtil.sendEmail(content, title, email);
    }
}
