package be15fintomatokatchupbe.client.query.controller;

import be15fintomatokatchupbe.client.query.dto.ClientCompanyDetailResponse;
import be15fintomatokatchupbe.client.query.service.ClientCompanyQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client-companies")
@RequiredArgsConstructor
public class ClientCompanyQueryController {

    private final ClientCompanyQueryService clientCompanyQueryService;

    @GetMapping("/{clientCompanyId}/detail")
    @Operation(summary = "고객사/사원 상세 조회", description = "고객사 정보 및 담당 매니저, 사원 목록을 함께 조회합니다.")
    public ResponseEntity<ApiResponse<ClientCompanyDetailResponse>> getClientCompanyDetail(
            @PathVariable Long clientCompanyId
    ) {
        ClientCompanyDetailResponse response = clientCompanyQueryService.getClientCompanyDetail(clientCompanyId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}