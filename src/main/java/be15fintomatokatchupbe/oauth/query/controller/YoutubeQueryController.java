package be15fintomatokatchupbe.oauth.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.oauth.query.dto.request.YoutubeCodeRequest;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeCodeResponse;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class YoutubeQueryController {

    private final YoutubeOAuthQueryService youtubeOAuthQueryService;
    private final YoutubeAnalyticsService youtubeAnalyticsService;

    // 1. 유튜브 권한 요청 URL 리디렉션
    @GetMapping("/authorize/youtube")
    public ResponseEntity<ApiResponse<String>> redirectToYoutubeAuthorization(
            HttpServletResponse response
    ) {
        String authUrl = youtubeOAuthQueryService.buildAuthorizationUrl();
//        response.sendRedirect(authUrl);
        return ResponseEntity.ok(ApiResponse.success(authUrl));
    }

    // 2. 인가코드 콜백 처리 및 accessToken + channelId 응답
    @PostMapping("/youtube/code")
    public ResponseEntity<ApiResponse<YoutubeCodeResponse>> handleYoutubeCallback(
            @RequestBody YoutubeCodeRequest request
    ) {
        log.info("code: {}", request.getCode());
        log.info("req: {}", request.toString());

        GoogleTokenResponse tokenResponse = youtubeOAuthQueryService.getToken(request.getCode());
        ChannelIdResponse channel = youtubeOAuthQueryService.getMyChannelIdPost(tokenResponse.getAccessToken());
        String channelId = channel.getItems().get(0).getId();

        youtubeOAuthQueryService.saveRefreshToken(channelId, tokenResponse);

        log.info("엑세스 토큰 : {}", tokenResponse.getAccessToken());
        log.info("리프레쉬 토큰 : {}", tokenResponse.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(YoutubeCodeResponse.builder().channelId(channelId).build()));
    }

//    @PostMapping("/oauth2/youtube/token")
//    public ResponseEntity<ApiResponse<Void>> exchangeToken() {
//        GoogleTokenResponse tokenResponse = youtubeOAuthQueryService.getAccessToken(request.getCode());
//        ChannelIdResponse channel = youtubeOAuthQueryService.getMyChannelId(tokenResponse.getAccessToken());
//        String channelId = channel.getItems().get(0).getId();
//
//        // refreshToken 저장
////        youtubeOAuthQueryService.saveOrUpdateRefreshToken(channelId, tokenResponse);
//
//        return ResponseEntity.ok(ApiResponse.success(null));
//    }

    // 3. 직접 Analytics API 호출 (Postman 테스트용)
    @GetMapping("/youtube/analytics")
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getAnalyticsDirect(
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
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }

    // 4. 통합된 통계 정보 반환 (서비스 연동용)
    @GetMapping("/youtube/stats")
    public ResponseEntity<ApiResponse<YoutubeStatsResponse>> getAggregatedStats(
            @RequestParam String accessToken,
            @RequestParam String channelId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        YoutubeStatsResponse stats = youtubeAnalyticsService.getYoutubeStats(accessToken, channelId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}