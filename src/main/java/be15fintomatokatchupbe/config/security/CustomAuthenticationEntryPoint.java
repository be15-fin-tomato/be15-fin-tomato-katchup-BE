package be15fintomatokatchupbe.config.security;


import be15fintomatokatchupbe.common.exception.GlobalErrorCode;
import be15fintomatokatchupbe.utils.jwt.JwtErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/* 인증 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtErrorResponse jwtErrorResponse;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        jwtErrorResponse.setErrorResponse(response, GlobalErrorCode.INVALID_TOKEN);

    }



}
