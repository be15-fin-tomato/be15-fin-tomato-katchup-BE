package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.dashboard.query.dto.response.SearchRatioResponse;
import be15fintomatokatchupbe.dashboard.query.service.SearchRatioQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대시보드 검색비율")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class SearchRatioQueryController {

    private final SearchRatioQueryService searchRatioQueryService;

    @Operation(summary = "캠페인별 검색비율 조회", description = "사용자는 캠페인별로 상품읲 검색비율을 조회할 수 있다.")
    @GetMapping("/search-ratio/{campaignId}")
    public ResponseEntity<SearchRatioResponse> getSearchRatioByCampaignId(@PathVariable Long campaignId) {
        SearchRatioResponse response = searchRatioQueryService.getSearchRatioByCampaignId(campaignId);
        return ResponseEntity.ok(response);
    }
}
