package be15fintomatokatchupbe.contract.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.contract.command.application.service.ContractSuccessCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "계약 완료 페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractSuccessCommandController {

    private final ContractSuccessCommandService commandService;

    @Operation(summary = "계약서 등록", description = "사용자는 완료된 계약서를 등록할 수 있다.")
    @PutMapping("/sign/{contractId}")
    public ResponseEntity<ApiResponse<Void>> signContract(
            @PathVariable Long contractId,
            @RequestPart(required = false) List<MultipartFile> files
            ){
        commandService.signContract(contractId, files);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
