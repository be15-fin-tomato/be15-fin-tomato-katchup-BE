package be15fintomatokatchupbe.contract.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.contract.query.dto.response.ContractPageResponse;
import be15fintomatokatchupbe.contract.query.dto.response.ObjectResponse;
import be15fintomatokatchupbe.contract.query.service.ContractQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "계약서 조회")
@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractQueryController {

    private final ContractQueryService contractQueryService;

    @Operation(summary = "계약서 조건 목록 조회", description = "사용자는 계약서 목적의 목록을 조회할 수 있다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ObjectResponse>>> getAllObjects() {
        List<ObjectResponse> objects = contractQueryService.getAllObjects();
        return ResponseEntity.ok(ApiResponse.success(objects));
    }

    @Operation(summary = "계약서 조건별 상세 조회", description = "사용자는 계약서를 조건별로 상세 조회할 수 있다.")
    @GetMapping("/{objectId}")
    public ResponseEntity<ApiResponse<ContractPageResponse>> getContractPage(
            @PathVariable Long objectId,
            @RequestParam(required = false) Long selectedDetailId
    ) {
        ContractPageResponse response = contractQueryService.getContractPage(objectId, selectedDetailId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

