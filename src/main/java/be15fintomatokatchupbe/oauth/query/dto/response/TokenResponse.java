package be15fintomatokatchupbe.oauth.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private int expiresIn; // 단위: 초
}
