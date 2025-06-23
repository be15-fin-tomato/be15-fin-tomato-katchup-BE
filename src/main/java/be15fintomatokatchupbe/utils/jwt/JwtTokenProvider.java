package be15fintomatokatchupbe.utils.jwt;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.common.exception.GlobalErrorCode;
import be15fintomatokatchupbe.common.exception.UnAuthorizationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    @Getter
    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        byte[] key = Decoders.BASE64.decode(jwtSecret);
        secretKey = Keys.hmacShaKeyFor(key);
    }

    public String createAccessToken(Long userId, String loginId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+ jwtExpiration);
        return Jwts.builder()
                .subject(loginId)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long userId, String loginId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+ jwtRefreshExpiration);
        return Jwts.builder()
                .subject(loginId)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new UnAuthorizationException(GlobalErrorCode.UNAUTHORIZED_REQUEST);
            /* 글로벌 단위에서 캐치 하는 중*/
        } catch (ExpiredJwtException e) {
            throw new BusinessException(GlobalErrorCode.INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new BadCredentialsException("지원하지 않는 요청입니다.", e);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("잘못된 요청입니다.", e);
        }
    }

    public String getLoginIdFromJWT(String token) {
        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Long getUserIdFromJWT(String token) {
        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }

    public Date getExpirationDate(String token) {
        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public Authentication getAuthentication(String token) {
        Long userId = getUserIdFromJWT(token);
        String loginId = getLoginIdFromJWT(token);

        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
