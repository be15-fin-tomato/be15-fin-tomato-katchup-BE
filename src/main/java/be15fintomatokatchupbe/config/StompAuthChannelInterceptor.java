package be15fintomatokatchupbe.config;

import be15fintomatokatchupbe.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        System.out.println("[StompAuthChannelInterceptor] Received STOMP Command: " + accessor.getCommand());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String header = accessor.getFirstNativeHeader("Authorization");
            System.out.println("[StompAuthChannelInterceptor] Authorization Header: " + (header != null ? header.substring(0, Math.min(header.length(), 20)) + "..." : "null"));

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                System.out.println("[StompAuthChannelInterceptor] Extracted Token: " + (token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : "null"));

                if (jwtTokenProvider.validateToken(token)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    accessor.setUser(authentication);
                    System.out.println("[StompAuthChannelInterceptor] STOMP CONNECT successful for user: " + authentication.getName());
                } else {
                    System.out.println("[StompAuthChannelInterceptor] Invalid JWT token for STOMP CONNECT. Token: " + (token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : "null"));
                    throw new IllegalArgumentException("Invalid JWT token.");
                }
            } else {
                System.out.println("[StompAuthChannelInterceptor] Missing Authorization header for STOMP CONNECT.");
                throw new IllegalArgumentException("Missing Authorization header.");
            }
        }

        return message;
    }
}
    