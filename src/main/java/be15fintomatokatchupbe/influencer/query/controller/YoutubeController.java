package be15fintomatokatchupbe.influencer.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.dashboard.query.service.SearchRatioQueryService;
import be15fintomatokatchupbe.influencer.query.service.YoutubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 요약")
@Slf4j
@RestController
@RequestMapping("/youtube")
@RequiredArgsConstructor
public class YoutubeController {

    private final SearchRatioQueryService searchRatioQueryService;
    private final YoutubeService youtubeService;

    @Operation(summary = "유튜브 댓글 요약", description = "캠페인에 등록된 유튜브 영상의 댓글을 AI 모델을 통해 요약하여 제공합니다.")
    @GetMapping("/commentssummary/{campaignId}")
    public ResponseEntity<ApiResponse<String>> summarizeCommentsByCampaignId(
            @PathVariable Long campaignId
    ) {
        String videoId = searchRatioQueryService.extractVideoIdByCampaignId(campaignId);
        String summary = youtubeService.summarizeCommentsByVideoId(videoId);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

}
