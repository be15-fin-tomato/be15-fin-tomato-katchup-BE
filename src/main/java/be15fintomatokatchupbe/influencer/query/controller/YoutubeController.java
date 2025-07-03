package be15fintomatokatchupbe.influencer.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.dashboard.query.service.SearchRatioQueryService;
import be15fintomatokatchupbe.influencer.query.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/youtube")
@RequiredArgsConstructor
public class YoutubeController {

    private final SearchRatioQueryService searchRatioQueryService;
    private final YoutubeService youtubeService;

    @GetMapping("/commentssummary/{campaignId}")
    public ResponseEntity<ApiResponse<List<String>>> getCommentsByCampaignId(
            @PathVariable Long campaignId
    ) {
        String videoId = searchRatioQueryService.extractVideoIdByCampaignId(campaignId);
        List<String> comments = youtubeService.getCommentsByVideoId(videoId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

}
