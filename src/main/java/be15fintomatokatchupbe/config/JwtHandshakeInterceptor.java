package be15fintomatokatchupbe.config;

import be15fintomatokatchupbe.utils.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        System.out.println("### JwtHandshakeInterceptor beforeHandshake method entered ###");

        try {
            if (request instanceof ServletServerHttpRequest servletRequest) {
                HttpServletRequest req = servletRequest.getServletRequest();

                String authHeader = req.getHeader("Authorization");
                String token = null;

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                } else {
                    token = req.getParameter("token");
                }

                System.out.println("--- Token received in HandshakeInterceptor: " + (token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : "null"));

                boolean isValid = false;
                if (token != null) {
                    isValid = jwtTokenProvider.validateToken(token);
                }
                System.out.println("--- Token validation result in HandshakeInterceptor: " + isValid);

                if (isValid) {
                    Long userId = jwtTokenProvider.getUserIdFromJWT(token);
                    attributes.put("userId", userId);
                    System.out.println("[JwtHandshakeInterceptor] Handshake successful for userId: " + userId);
                    return true;
                } else {
                    System.out.println("[JwtHandshakeInterceptor] Token missing or invalid. Handshake failed.");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }
            }
        } catch (Exception e) {
            System.err.println("[JwtHandshakeInterceptor] Error during handshake: " + e.getMessage());
            e.printStackTrace();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return false;
        }
        System.out.println("[JwtHandshakeInterceptor] Not a ServletServerHttpRequest. Handshake failed.");
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        if (exception != null) {
            System.err.println("[JwtHandshakeInterceptor] After handshake exception: " + exception.getMessage());
        }
    }
}
