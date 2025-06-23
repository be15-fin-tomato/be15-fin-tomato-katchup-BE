package be15fintomatokatchupbe.client.command.application.controller;

import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientCompanyRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.UpdateClientCompanyRequest;
import be15fintomatokatchupbe.client.command.application.service.ClientCompanyCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client-companies")
@RequiredArgsConstructor
@Tag(name = "ClientCompanyCommand", description = "고객사 등록 관련 API")
public class ClientCompanyCommandController {

    private final ClientCompanyCommandService clientCompanyCommandService;

    @PostMapping
    @Operation(summary = "고객사 등록", description = "고객사와 해당 소속 사원들을 함께 등록합니다.")
    public ResponseEntity<ApiResponse<Void>> createClientCompany(
            @Valid @RequestBody CreateClientCompanyRequest request) {
        clientCompanyCommandService.createClientCompany(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{clientCompanyId}")
    @Operation(summary = "고객사 정보 수정", description = "고객사 및 사원 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<Void>> updateClientCompany(
            @PathVariable Long clientCompanyId,
            @Valid @RequestBody UpdateClientCompanyRequest request) {
        clientCompanyCommandService.updateClientCompany(clientCompanyId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{clientCompanyId}")
    @Operation(summary = "고객사 삭제", description = "고객사와 연결된 소속 사원들은 soft delete 되고, 담당자 관계는 hard delete 됩니다.")
    public ResponseEntity<ApiResponse<Void>> deleteClientCompany(
            @PathVariable Long clientCompanyId
    ) {
        clientCompanyCommandService.deleteClientCompany(clientCompanyId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}