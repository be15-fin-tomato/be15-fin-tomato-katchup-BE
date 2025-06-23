package be15fintomatokatchupbe.email.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.email.query.dto.request.EmailSearchRequest;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponse;
import be15fintomatokatchupbe.email.query.service.EmailQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<ApiResponse<CampaignSatisfactionResponse>> getCampaignSatisfaction(
            EmailSearchRequest emailSearchRequest
    ) {

        CampaignSatisfactionResponse response = emailQueryService.getCampaignSatisfaction(emailSearchRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
