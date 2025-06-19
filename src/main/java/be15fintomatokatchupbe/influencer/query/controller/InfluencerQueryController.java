package be15fintomatokatchupbe.influencer.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.influencer.query.dto.request.InfluencerListRequestDTO;
import be15fintomatokatchupbe.influencer.query.service.InfluencerQueryService;
import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/influencer")
@RequiredArgsConstructor
public class InfluencerQueryController {

    private final InfluencerQueryService influencerQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<InfluencerListResponse>> getInfluencerList(@ModelAttribute InfluencerListRequestDTO request) {
        InfluencerListResponse response = influencerQueryService.getInfluencers(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
