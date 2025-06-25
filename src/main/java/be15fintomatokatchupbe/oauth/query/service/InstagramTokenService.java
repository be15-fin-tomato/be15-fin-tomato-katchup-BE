package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.oauth.query.dto.response.InstagramTokenResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramTokenService {
    @Value("${facebook.client-id}")
    private String clientId;

    @Value("${facebook.client-secret}")
    private String clientSecret;

    @Value("${facebook.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public InstagramTokenResponse exchangeCodeForToken(String code) {
        try {
            // 1. code → access token 요청
            String tokenUrl = "https://graph.facebook.com/v23.0/oauth/access_token"
                    + "?client_id=" + clientId
                    + "&client_secret=" + clientSecret
                    + "&redirect_uri=" + redirectUri
                    + "&code=" + code;

            String tokenBody = webClient.get().uri(tokenUrl).retrieve().bodyToMono(String.class).block();
            JsonNode tokenJson = objectMapper.readTree(tokenBody);
            String accessToken = tokenJson.get("access_token").asText();

            // 2. accessToken → 페이지 ID 요청
            String pageUrl = "https://graph.facebook.com/v23.0/me/accounts?access_token=" + accessToken;
            String pageBody = webClient.get().uri(pageUrl).retrieve().bodyToMono(String.class).block();
            JsonNode pageJson = objectMapper.readTree(pageBody);
            String pageId = pageJson.path("data").get(0).path("id").asText();

            // 3. 페이지 ID → instagram_business_account 조회
            String igUrl = "https://graph.facebook.com/v23.0/" + pageId + "?fields=instagram_business_account&access_token=" + accessToken;
            String igBody = webClient.get().uri(igUrl).retrieve().bodyToMono(String.class).block();
            JsonNode igJson = objectMapper.readTree(igBody);
            String igAccountId = igJson.path("instagram_business_account").path("id").asText();

            // 결과 반환
            return new InstagramTokenResponse(accessToken, igAccountId);

        } catch (Exception e) {
            log.error("[InstagramTokenService] 코드 교환 실패", e);
            throw new RuntimeException("Instagram token exchange failed");
        }
    }
}
