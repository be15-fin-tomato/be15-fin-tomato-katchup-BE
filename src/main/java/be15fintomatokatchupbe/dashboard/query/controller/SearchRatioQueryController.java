package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.dashboard.query.dto.response.SearchRatioResponse;
import be15fintomatokatchupbe.dashboard.query.service.SearchRatioQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class SearchRatioQueryController {

    private final SearchRatioQueryService searchRatioQueryService;

    @GetMapping("/search-ratio/{campaignId}")
    public ResponseEntity<SearchRatioResponse> getSearchRatioByCampaignId(@PathVariable Long campaignId) {
        SearchRatioResponse response = searchRatioQueryService.getSearchRatioByCampaignId(campaignId);
        return ResponseEntity.ok(response);
    }
}
