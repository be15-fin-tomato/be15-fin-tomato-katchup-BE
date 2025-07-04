package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Instagram;
import be15fintomatokatchupbe.influencer.command.domain.repository.InfluencerRepository;
import be15fintomatokatchupbe.influencer.command.domain.repository.InstagramRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.infra.redis.InstagramTokenRepository;
import be15fintomatokatchupbe.oauth.command.application.dto.InstagramAccountInfo;
import be15fintomatokatchupbe.oauth.exception.OAuthErrorCode;
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

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final InstagramTokenRepository instagramTokenRepository;
    private final InstagramRepository instagramRepository;
    private final InfluencerRepository influencerRepository;

    @Value("${facebook.client-id}")
    private String clientId;

    @Value("${facebook.client-secret}")
    private String clientSecret;

    @Value("${facebook.redirect-uri}")
    private String redirectUri;

    private static final int LONG_LIVED_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24 * 60; // 5184000초

    public InstagramTokenResponse exchangeCodeForToken(String code, Long influencerId) {
        try {
            // 1. Short-lived token 발급
            String tokenUrl = String.format(
                    "https://graph.facebook.com/v23.0/oauth/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s",
                    clientId, clientSecret, redirectUri, code
            );
            String tokenBody = webClient.get().uri(tokenUrl).retrieve().bodyToMono(String.class).block();
            String shortLivedToken = objectMapper.readTree(tokenBody).path("access_token").asText();

            // 2. long-lived token 으로 교환
            String longLivedToken = exchangeForLongLivedToken(shortLivedToken);

            // 3. 페이지 ID 조회
            String pageUrl = "https://graph.facebook.com/v23.0/me/accounts?access_token=" + longLivedToken;
            String pageBody = webClient.get().uri(pageUrl).retrieve().bodyToMono(String.class).block();
            String pageId = objectMapper.readTree(pageBody).path("data").get(0).path("id").asText();

            // 4.Business Account ID 조회
            String igUrl = "https://graph.facebook.com/v23.0/" + pageId + "?fields=instagram_business_account&access_token=" + longLivedToken;
            String igBody = webClient.get().uri(igUrl).retrieve().bodyToMono(String.class).block();
            String igAccountId = objectMapper.readTree(igBody).path("instagram_business_account").path("id").asText();

            // 5. Redis에 long-lived token 저장
            instagramTokenRepository.saveAccessToken(igAccountId, longLivedToken, LONG_LIVED_TOKEN_EXPIRE_SECONDS);

            InstagramAccountInfo info = fetchAccountInfo(longLivedToken, igAccountId);

            Influencer influencer = influencerRepository.findByIdAndIsDeleted(influencerId, StatusType.N)
                    .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

            Instagram instagram = Instagram.builder()
                    .accountId(info.getAccountId())
                    .name(info.getName())
                    .follower(info.getFollower())
                    .influencerId(influencer.getId())
                    .build();

            instagramRepository.save(instagram);

            return new InstagramTokenResponse(longLivedToken, igAccountId);

        } catch (Exception e) {
            log.error("[InstagramTokenService] 인스타그램 토큰 교환 실패", e);
            throw new BusinessException(OAuthErrorCode.FAILED_TOKEN_EXCHANGE);
        }
    }

    public String exchangeForLongLivedToken(String shortLivedToken) {
        try {
            String url = String.format(
                    "https://graph.facebook.com/v17.0/oauth/access_token?grant_type=fb_exchange_token&client_id=%s&client_secret=%s&fb_exchange_token=%s",
                    clientId, clientSecret, shortLivedToken
            );
            String response = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
            return objectMapper.readTree(response).path("access_token").asText();
        } catch (Exception e) {
            log.error("[InstagramTokenService] 롱 리브 액세스 토큰 발급 실패", e);
            throw new BusinessException(OAuthErrorCode.LONG_LIVED_TOKEN_EXCHANGE_FAILED);
        }
    }

    public String refreshLongLivedToken(String currentLongLivedToken) {
        try {
            String url = "https://graph.facebook.com/v17.0/refresh_access_token"
                    + "?grant_type=ig_refresh_token"
                    + "&access_token=" + currentLongLivedToken;

            String response = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
            return objectMapper.readTree(response).path("access_token").asText();
        } catch (Exception e) {
            log.error("[InstagramTokenService] 롱 리브 토큰 갱신 실패", e);
            throw new BusinessException(OAuthErrorCode.LONG_LIVED_TOKEN_REFRESH_FAILED);
        }
    }

    private InstagramAccountInfo fetchAccountInfo(String token, String igAccountId) {
        String url = String.format("https://graph.facebook.com/v23.0/%s?fields=id,username,followers_count&access_token=%s", igAccountId, token);
        try {
            String response = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
            JsonNode node = objectMapper.readTree(response);

            return new InstagramAccountInfo(
                    node.path("id").asText(),
                    node.path("username").asText(),
                    node.path("followers_count").asLong()
            );
        } catch (Exception e) {
            log.error("Instagram 계정 정보 조회 실패: {}", e.getMessage());
            throw new BusinessException(OAuthErrorCode.INSTAGRAM_ACCOUNT_INFO_ERROR);
        }
    }
}
