package be15fintomatokatchupbe.client.query.controller;

import be15fintomatokatchupbe.client.query.dto.ClientManagerSimpleResponse;
import be15fintomatokatchupbe.client.query.service.ClientManagerQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client-companies")
@RequiredArgsConstructor
public class ClientManagerQueryController {

    private final ClientManagerQueryService clientManagerQueryService;

    @GetMapping("/{clientCompanyId}/managers")
    @Operation(summary = "고객사 사원 목록 조회", description = "고객사에 소속된 사원들의 간단한 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ClientManagerSimpleResponse>>> getManagersByClientCompanyId(
            @PathVariable Long clientCompanyId
    ) {
        List<ClientManagerSimpleResponse> result = clientManagerQueryService.getManagersByClientCompanyId(clientCompanyId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}