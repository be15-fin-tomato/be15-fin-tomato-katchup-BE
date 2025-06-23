package be15fintomatokatchupbe.contract.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.contract.command.application.dto.request.DigitalContractEditRequest;
import be15fintomatokatchupbe.contract.command.application.dto.response.DigitalContractDeleteResponse;
import be15fintomatokatchupbe.contract.command.application.dto.response.DigitalContractEditResponse;
import be15fintomatokatchupbe.contract.command.application.service.DigitalContractCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/digitalcontract")
@RequiredArgsConstructor
public class DigitalContractCommandController {

    private final DigitalContractCommandService digitalContractCommandService;

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<DigitalContractEditResponse>> editDigitalContract(
            @RequestBody DigitalContractEditRequest request
            ){
        DigitalContractEditResponse response = digitalContractCommandService.editDigitalContract(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/delete/{digitalContractId}")
    public ResponseEntity<ApiResponse<DigitalContractDeleteResponse>> deleteDigitalContract(
            @PathVariable Long digitalContractId
    ) {
        DigitalContractDeleteResponse response = digitalContractCommandService.deleteDigitalContract(digitalContractId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
