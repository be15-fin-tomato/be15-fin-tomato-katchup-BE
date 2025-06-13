package be15fintomatokatchupbe.config.security;


import be15fintomatokatchupbe.common.exception.ErrorCode;
import be15fintomatokatchupbe.common.exception.GlobalErrorCode;
import be15fintomatokatchupbe.utils.jwt.JwtErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final JwtErrorResponse jwtErrorResponse;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = GlobalErrorCode.ACCESS_DENIED;

        jwtErrorResponse.setErrorResponse(response, errorCode);
    }
}
