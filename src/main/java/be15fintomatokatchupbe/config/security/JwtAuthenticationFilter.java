package be15fintomatokatchupbe.config.security;

import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.utils.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String upgradeHeader = request.getHeader("Upgrade");
        if (upgradeHeader != null && "websocket".equalsIgnoreCase(upgradeHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = parseToken(request);

        if(token != null && jwtTokenProvider.validateToken(token)){
            Long userId = jwtTokenProvider.getUserIdFromJWT(token);
            String loginId = jwtTokenProvider.getLoginIdFromJWT(token);

            CustomUserDetail userDetail = CustomUserDetail.builder()
                    .userId(userId)
                    .loginId(loginId)
                    .build();
            PreAuthenticatedAuthenticationToken authentication
                    = new PreAuthenticatedAuthenticationToken(
                    userDetail, null,userDetail.getAuthorities()
            );
            log.info("[Jwt Filter] 인증된 유저: userId={}, loginId={}", userId, loginId);


            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
