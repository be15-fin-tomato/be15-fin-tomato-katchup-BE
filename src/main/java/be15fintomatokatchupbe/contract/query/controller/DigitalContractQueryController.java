package be15fintomatokatchupbe.contract.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.contract.query.dto.request.DigitalContractDetailRequest;
import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractDetailResponse;
import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractListResponse;
import be15fintomatokatchupbe.contract.query.service.DigitalContractQueryService;
import be15fintomatokatchupbe.influencer.query.dto.request.InfluencerListRequestDTO;
import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerListResponse;
import be15fintomatokatchupbe.influencer.query.service.InfluencerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/digitalcontract")
@RequiredArgsConstructor
public class DigitalContractQueryController {

    private final DigitalContractQueryService digitalContractQueryService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<DigitalContractListResponse>> getDigitalContractList() {
        DigitalContractListResponse response = digitalContractQueryService.getDigital();
        return ResponseEntity.ok(ApiResponse.success(response));

    }
    @GetMapping("/detail/{digitalContractId}")
    public ResponseEntity<ApiResponse<DigitalContractDetailResponse>> getDigitalContractDetail(
            @PathVariable Long digitalContractId
    ) {
        DigitalContractDetailResponse response = digitalContractQueryService.getDigitalDetail(digitalContractId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}