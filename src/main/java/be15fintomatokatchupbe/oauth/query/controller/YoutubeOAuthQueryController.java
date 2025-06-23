package be15fintomatokatchupbe.oauth.query.controller;

import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeAnalyticsService;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.AnalyticsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.ChannelIdResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.GoogleTokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class YoutubeOAuthQueryController {

    private final YoutubeOAuthQueryService youtubeOAuthQueryService;
    private final YoutubeAnalyticsService youtubeAnalyticsService;

    // 1. 유튜브 권한 요청 URL 리디렉션
    @GetMapping("/authorize/youtube")
    public void redirectToYoutubeAuthorization(HttpServletResponse response) throws IOException {
        response.sendRedirect(youtubeOAuthQueryService.buildAuthorizationUrl());
    }

    // 2. 인가코드 콜백 처리 및 accessToken + channelId 응답
    @GetMapping("/callback/youtube")
    public ResponseEntity<Map<String, String>> handleYoutubeCallback(@RequestParam String code) {
        GoogleTokenResponse tokenResponse = youtubeOAuthQueryService.getAccessToken(code);
        ChannelIdResponse channel = youtubeOAuthQueryService.getMyChannelId(tokenResponse.getAccessToken());
        String channelId = channel.getItems().get(0).getId();

//        youtubeOAuthQueryService.saveOrUpdateRefreshToken(channelId, tokenResponse);

        log.info("accessToken" + tokenResponse.getAccessToken());
        log.info("refreshToken" + tokenResponse.getRefreshToken());
        log.info("channelID" + channelId);

        return ResponseEntity.ok(Map.of(
                "accessToken", tokenResponse.getAccessToken(),
                "refreshToken", tokenResponse.getRefreshToken(),
                "channelId", channelId
        ));
    }

    // 3. 직접 Analytics API 호출 (Postman 테스트용)
    @GetMapping("/youtube/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalyticsDirect(
            @RequestParam String accessToken,
            @RequestParam String channelId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "views,comments,likes,estimatedMinutesWatched") String metrics,
            @RequestParam(defaultValue = "day") String dimensions,
            @RequestParam(required = false) String filters
    ) {
        AnalyticsResponse analytics = youtubeOAuthQueryService.getChannelAnalytics(
                accessToken, channelId, startDate, endDate, metrics, dimensions, filters
        );
        return ResponseEntity.ok(analytics);
    }

    // 4. 통합된 통계 정보 반환 (서비스 연동용)
    @GetMapping("/youtube/stats")
    public ResponseEntity<YoutubeStatsResponse> getAggregatedStats(
            @RequestParam String accessToken,
            @RequestParam String channelId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        YoutubeStatsResponse stats = youtubeAnalyticsService.getYoutubeStats(accessToken, channelId, startDate, endDate);
        return ResponseEntity.ok(stats);
    }
}