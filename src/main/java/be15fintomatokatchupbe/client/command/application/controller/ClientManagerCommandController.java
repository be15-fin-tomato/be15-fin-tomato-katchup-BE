package be15fintomatokatchupbe.client.command.application.controller;

import be15fintomatokatchupbe.client.command.application.service.ClientManagerCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client-managers")
@RequiredArgsConstructor
@Tag(name = "ClientManagerCommand", description = "고객사 사원 관련 API")
public class ClientManagerCommandController {

    private final ClientManagerCommandService clientManagerCommandService;

    @DeleteMapping("/{clientManagerId}")
    @Operation(summary = "고객사 사원 삭제", description = "사원을 단독으로 소프트 딜리트 합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteClientManager(
            @PathVariable Long clientManagerId
    ) {
        clientManagerCommandService.deleteClientManager(clientManagerId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
