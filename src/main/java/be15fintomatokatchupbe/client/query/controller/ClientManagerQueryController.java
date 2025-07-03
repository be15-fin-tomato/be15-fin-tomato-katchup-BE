package be15fintomatokatchupbe.client.query.controller;

import be15fintomatokatchupbe.client.query.dto.ClientManagerListResponse;
import be15fintomatokatchupbe.client.query.dto.ClientManagerSimpleResponse;
import be15fintomatokatchupbe.client.query.service.ClientManagerQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/manager/search")
    @Operation(summary = "고객 목록 검색", description = "사용자는 유저 목록을 조회할 수 있습니다.")
    public ResponseEntity<ApiResponse<ClientManagerListResponse>> findClientManagerList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(required = false) String keyword
    ){
        ClientManagerListResponse response = clientManagerQueryService.getClientManagerList(keyword);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}