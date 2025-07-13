package be15fintomatokatchupbe.oauth.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeStatsSnapshot;
import be15fintomatokatchupbe.oauth.query.dto.YoutubeVideoInfo;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeAnalyticsQueryService;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService;
import be15fintomatokatchupbe.oauth.query.service.YoutubeSnapshotQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "YouTube OAuth", description = "YouTube 연동 및 통계 조회 API")
@Slf4j
@RestController
@RequestMapping("/oauth2/youtube")
@RequiredArgsConstructor
public class YoutubeQueryController {

    private final YoutubeOAuthQueryService youtubeOAuthQueryService;
    private final YoutubeAnalyticsQueryService youtubeAnalyticsQueryService;
    private final YoutubeSnapshotQueryService youtubeSnapshotQueryService;

    @GetMapping("/auth-url/{influencerId}")
    public ResponseEntity<ApiResponse<String>> getYoutubeAuthUrl(
            @PathVariable Long influencerId
    ) {
        String authUrl = youtubeOAuthQueryService.buildAuthorizationUrl(influencerId);
        return ResponseEntity.ok(ApiResponse.success(authUrl));
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<String>> registerYoutube(
            @RequestParam("code") String code,
            @RequestParam("state") Long influencerId
    ) {
        youtubeOAuthQueryService.registerYoutubeByOAuth(code, influencerId);
        return ResponseEntity.ok(ApiResponse.success("✅ 유튜브 계정 연동 완료"));
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

    @Operation(
            summary = "YouTube 테이블 조회",
            description = "YouTube 채널에 대한 통계 정보를 통합 조회합니다. (조회수, 좋아요 수, 댓글 수, 연령대, 성별 비율, 인기 영상 등)"
    )
    @GetMapping("/channel-info")
    public ResponseEntity<ApiResponse<List<YoutubeStatsSnapshot>>> getChannelInfo(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam Long influencerId
    ) {
        List<YoutubeStatsSnapshot> stats = youtubeSnapshotQueryService.getStatsByInfluencer(influencerId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/top-videos")
    public ResponseEntity<ApiResponse<List<YoutubeVideoInfo>>> getTopVideos(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam Long influencerId
    ) {
        List<YoutubeVideoInfo> stats = youtubeSnapshotQueryService.getTopVideosByInfluencer(influencerId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
