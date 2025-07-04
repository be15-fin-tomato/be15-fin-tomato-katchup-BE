package be15fintomatokatchupbe.email.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.email.command.application.service.EmailCommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "만족도 조사")
@RestController
@RequiredArgsConstructor
@RequestMapping("/satisfaction")
public class EmailCommandController {

    private final EmailCommendService emailCommendService;

    /* 만족도 조사 요청 */
    @PostMapping("/send/{satisfactionId}")
    @Operation(summary = "만족도 조사 이메일 요청", description = "만족도 조사 구글폼을 요청할 수 있다.")
    public ResponseEntity<ApiResponse<Void>> sendSatisfaction (
            @PathVariable Long satisfactionId
    ) {
        emailCommendService.sendSatisfaction(satisfactionId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 만족도 결과 저장하기 */
    @PostMapping("/save/{satisfactionId}")
    @Operation(summary = "만족도 조사 결과 불러오기", description = "만족도 조사 총 점수를 불러올 수 있다.")
    public ResponseEntity<ApiResponse<Void>> getSatisfactionResult (
            @PathVariable Long satisfactionId
    ) {
        emailCommendService.getSatisfactionResult(satisfactionId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

}