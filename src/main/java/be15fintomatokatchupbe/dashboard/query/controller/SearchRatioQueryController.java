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

    @Operation(summary = "인플루언서별 검색비율 조회", description = "사용자는 pipelineInfluencerId를 기준으로 상품의 검색비율을 조회할 수 있다.")
    @GetMapping("/search-ratio/pipeline-influencer/{pipelineInfluencerId}")
    public ResponseEntity<SearchRatioResponse> getSearchRatioByPipelineInfluencerId(@PathVariable Long pipelineInfluencerId) {
        SearchRatioResponse response = searchRatioQueryService.getSearchRatioByPipelineInfluencerId(pipelineInfluencerId);
        return ResponseEntity.ok(response);
    }
}
