package be15fintomatokatchupbe.oauth.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.oauth.query.dto.InstagramFullSnapshot;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramPostInsightResponse;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramStatsResponse;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramTokenResponse;
import be15fintomatokatchupbe.oauth.query.service.InstagramPostQueryService;
import be15fintomatokatchupbe.oauth.query.service.InstagramAccountQueryService;
import be15fintomatokatchupbe.oauth.query.service.InstagramStatsSnapshotService;
import be15fintomatokatchupbe.oauth.query.service.InstagramTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Instagram OAuth", description = "인스타그램 OAuth 인증 및 통계 API")
@RestController
@RequestMapping("/oauth2/instagram")
@RequiredArgsConstructor
@Slf4j
public class
InstagramQueryController {
    private final InstagramPostQueryService instagramPostQueryService;
    private final InstagramAccountQueryService instagramAccountQueryService;
    private final InstagramTokenService instagramTokenService;
    private final InstagramStatsSnapshotService instagramStatsSnapshotService;

    // TODO: 프론트에서 처리해야됨 (지금은 백엔드 테스트용)
    @Operation(summary = "OAuth 콜백: code로 토큰 발급 및 프론트 리디렉션", description = "사용자는 인가 코드를 받아 액세스 토큰을 발급 받을 수 있다.")
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<InstagramTokenResponse>> handleInstagramCallback(
            @RequestParam("code") String code,
            @RequestParam("state") Long influencerId
    ) {
        InstagramTokenResponse tokenResponse = instagramTokenService.exchangeCodeForToken(code, influencerId);
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @Operation(summary = "인스타그램 계정 통계 조회", description = "사용자는 인플루언서의 인스타그램 계정 통계 자료를 조회할 수 있다.")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<InstagramStatsResponse>> fetchInstagramStats(
            @RequestParam Long influencerId
    ) {
        InstagramStatsResponse response = instagramAccountQueryService.fetchStats(influencerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "인스타그램 게시물 조회", description = "사용자는 인플루언서의 인스타그램 게시물 통계 자료를 조회할 수 있다.")
    @GetMapping("/insight")
    public ResponseEntity<ApiResponse<InstagramPostInsightResponse>> getPostInsight(
            @RequestParam Long pipelineInfluencerId
    ) {
        InstagramPostInsightResponse response = instagramPostQueryService
                .fetchPostInsightsByPipelineInfluencerId(pipelineInfluencerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "인스타그램 댓글 요약", description = "사용자는 해당 게시물의 요약된 댓글을 조회할 수 있다.")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<String>> getInstagramCommentSummary(
            @RequestParam Long pipelineInfluencerId
    ) {
        String summary = instagramPostQueryService.summarizeInstagramCommentsByPipelineInfluencerId(pipelineInfluencerId);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @Operation(summary = "인스타그램 계정 정보 조회", description = "사용자는 인플루언서의 인스타그램 계정 정보와 인기 게시물을 조회할 수 있다.")
    @GetMapping("/account-info")
    public ResponseEntity<ApiResponse<Optional<InstagramFullSnapshot>>> getSnapshot(
            @RequestParam Long influencerId
    ) {
        Optional<InstagramFullSnapshot> response = instagramStatsSnapshotService.getLatestInstagramFullSnapshotByInfluencer(influencerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
