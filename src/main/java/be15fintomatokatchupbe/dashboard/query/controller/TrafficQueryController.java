package be15fintomatokatchupbe.dashboard.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.TrafficResponse;
import be15fintomatokatchupbe.dashboard.query.service.TrafficQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "대시보드 트래픽 조회")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class TrafficQueryController {

    private final TrafficQueryService trafficQueryService;

    @Operation(summary = "인플루언서 별 트래픽 조회", description = "사용자는 플랫폼 대시보드에서 특정 인플루언서와 관련된 트래픽 데이터를 조회할 수 있다.") // API 설명
    @GetMapping("/traffic/{pipelineInfluencerId}")
    public ResponseEntity<ApiResponse<List<TrafficResponse>>> getTrafficByPipelineInfluencer(
            @PathVariable Long pipelineInfluencerId) {

        List<TrafficResponse> trafficResponses = trafficQueryService.getTrafficByPipelineInfluencerId(pipelineInfluencerId);

        return ResponseEntity.ok(ApiResponse.success(trafficResponses));
    }
}