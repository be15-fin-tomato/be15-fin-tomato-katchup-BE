package be15fintomatokatchupbe.client.query.controller;

import be15fintomatokatchupbe.client.query.dto.ClientCompanyDetailResponse;
import be15fintomatokatchupbe.client.query.service.ClientCompanyQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client-companies")
@RequiredArgsConstructor
public class ClientCompanyQueryController {

    private final ClientCompanyQueryService queryService;

    @GetMapping("/{clientCompanyId}/detail")
    @Operation(summary = "고객사 상세 조회", description = "고객사 및 사원 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<ClientCompanyDetailResponse>> getClientCompanyDetail(
            @PathVariable("clientCompanyId") Long clientCompanyId) {

        ClientCompanyDetailResponse response = queryService.getDetail(clientCompanyId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

