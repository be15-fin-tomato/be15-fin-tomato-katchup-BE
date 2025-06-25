package be15fintomatokatchupbe.oauth.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstagramTokenResponse {
    private String accessToken;
    private String igAccountId;
}
