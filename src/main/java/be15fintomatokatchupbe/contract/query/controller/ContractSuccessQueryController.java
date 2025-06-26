package be15fintomatokatchupbe.contract.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.contract.query.dto.request.ContractSuccessRequest;
import be15fintomatokatchupbe.contract.query.dto.response.ContractSuccessResponse;
import be15fintomatokatchupbe.contract.query.service.ContractSuccessQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "계약 완료 페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractSuccessQueryController {

    private final ContractSuccessQueryService contractSuccessQueryService;


    @Operation(summary = "계약 완료 목록 조회",description = "사용자는 계약이 완료된 목록을 조회할 수 있다.")
    @GetMapping("/success")
    public ResponseEntity<ApiResponse<ContractSuccessResponse>> getContractSuccess(
            @ModelAttribute ContractSuccessRequest request
            ) {
        ContractSuccessResponse response = contractSuccessQueryService.getContractSuccess(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
