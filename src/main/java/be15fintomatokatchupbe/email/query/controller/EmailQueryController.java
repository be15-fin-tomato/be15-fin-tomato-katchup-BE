package be15fintomatokatchupbe.email.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.email.query.dto.request.EmailSearchRequest;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponse;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponseDTO;
import be15fintomatokatchupbe.email.query.dto.response.SatisfactionAnswerResponse;
import be15fintomatokatchupbe.email.query.service.EmailQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "만족도 조사")
@RestController
@RequiredArgsConstructor
@RequestMapping("/satisfaction")
public class EmailQueryController {

    private final EmailQueryService emailQueryService;

    /* 만족도 조사 목록 조회 */
    @GetMapping("/list")
    @Operation(summary = "만족도 조사 목록조회", description = "만족도 조사 목록 조회를 할 수 있다.")
    public ResponseEntity<ApiResponse<CampaignSatisfactionResponse>> getCampaignSatisfaction(
            @ModelAttribute EmailSearchRequest emailSearchRequest
    ) {

        CampaignSatisfactionResponse response = emailQueryService.getCampaignSatisfaction(emailSearchRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 만족도 응답률 목록 조회 */
    @GetMapping("/response")
    @Operation(summary = "만족도 조사 응답률 조회", description = "만족도 조사 응답률을 조회할 수 있다.")
    public ResponseEntity<ApiResponse<CampaignSatisfactionResponseDTO>> getCampaignSatisfactionResponse() {

        CampaignSatisfactionResponseDTO response = emailQueryService.getCampaignSatisfactionResponse();

        return  ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 만족도 점수 평균 조회 */
    @GetMapping("/average")
    @Operation(summary = "만족도 점수 평균 조회", description = "만족도 조사 평균 점수를 조회할 수 있다.")
    public ResponseEntity<ApiResponse<Double>> getCampaignSatisfactionAverage() {

        double avg = emailQueryService.getCampaignSatisfactionAverage();

        return ResponseEntity.ok(ApiResponse.success(avg));
    }

    /* 만족도 항목별 점수 조회 */
    @GetMapping("/list/score/{satisfactionId}")
    @Operation(summary = "만족도 항목별 점수 조회", description = "받은 만족도 점수를 항목별로 조회할 수 있다.")
    public ResponseEntity<ApiResponse<SatisfactionAnswerResponse>> getCampaignSatisfactionScore(
            @PathVariable Long satisfactionId
    ) {
        SatisfactionAnswerResponse response = emailQueryService.getCampaignSatisfactionScore(satisfactionId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
