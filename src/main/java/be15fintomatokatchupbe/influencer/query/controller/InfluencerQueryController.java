package be15fintomatokatchupbe.influencer.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerSearchResponse;
import be15fintomatokatchupbe.influencer.query.dto.request.InfluencerListRequestDTO;
import be15fintomatokatchupbe.influencer.query.service.InfluencerQueryService;
import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인플루언서 조회")
@RestController
@RequestMapping("/influencer")
@RequiredArgsConstructor
public class InfluencerQueryController {

    private final InfluencerQueryService influencerQueryService;

    @Operation(summary = "인플루언서 목록 조회", description = "사용자는 등록된 인플루언서의 목록을 조회할 수 있다.")
    @GetMapping
    public ResponseEntity<ApiResponse<InfluencerListResponse>> getInfluencerList(@ModelAttribute InfluencerListRequestDTO request) {
        InfluencerListResponse response = influencerQueryService.getInfluencers(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "인플루언서 검색", description = "사용자는 키워드로 인플루언서를 검색할 수 있다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<InfluencerSearchResponse>> findInfluencerList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(required = false) String keyword
            ){

        InfluencerSearchResponse response = influencerQueryService.findInfluencerList(keyword);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
