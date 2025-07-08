package be15fintomatokatchupbe.oauth.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.oauth.query.dto.request.YoutubeCodeRequest;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeCodeResponse;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeAnalyticsQueryService;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.AnalyticsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.ChannelIdResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.GoogleTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "YouTube OAuth", description = "YouTube 연동 및 통계 조회 API")
@Slf4j
@RestController
@RequestMapping("/oauth2/youtube")
@RequiredArgsConstructor
public class YoutubeQueryController {

    private final YoutubeOAuthQueryService youtubeOAuthQueryService;
    private final YoutubeAnalyticsQueryService youtubeAnalyticsQueryService;

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<String>> registerYoutube(
            @RequestParam("code") String code,
            @RequestParam("state") Long influencerId
    ) {
        youtubeOAuthQueryService.registerYoutubeByOAuth(code, influencerId);
        return ResponseEntity.ok(ApiResponse.success("✅ 유튜브 계정 연동 완료"));
    }

    @Operation(summary = "YouTube 권한 요청 URL 발급", description = "클라이언트가 YouTube OAuth 인증을 시작하기 위한 URL을 발급받습니다.")
    @GetMapping("/authorize/youtube")
    public ResponseEntity<ApiResponse<String>> redirectToYoutubeAuthorization(
            HttpServletResponse response
    ) {
        String authUrl = youtubeOAuthQueryService.buildAuthorizationUrl();
//        response.sendRedirect(authUrl);
        return ResponseEntity.ok(ApiResponse.success(authUrl));
    }

    @Operation(summary = "YouTube 인가코드 처리 및 채널 ID 응답", description = "YouTube에서 전달받은 인가코드(code)를 사용하여 access_token과 channelId를 발급받고 저장합니다.")
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

    @Operation(summary = "YouTube Analytics 직접 호출 (테스트용)", description = "Postman 등에서 access_token으로 직접 Analytics API를 호출합니다.")
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

    @Operation(summary = "YouTube 통합 통계 조회", description = "YouTube 채널에 대한 통계 정보를 통합 조회합니다. (조회수, 좋아요 수 등)")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<YoutubeStatsResponse>> getAggregatedStats(
            @RequestParam Long influencerId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        YoutubeStatsResponse stats = youtubeAnalyticsQueryService.getYoutubeStatsByInfluencer(influencerId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }


}