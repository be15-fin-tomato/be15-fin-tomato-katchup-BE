package be15fintomatokatchupbe.contract.query.controller;

import be15fintomatokatchupbe.contract.query.dto.response.ContractPageResponse;
import be15fintomatokatchupbe.contract.query.dto.response.ObjectResponse;
import be15fintomatokatchupbe.contract.query.service.ContractQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractQueryController {

    private final ContractQueryService contractQueryService;

    @GetMapping
    public ResponseEntity<List<ObjectResponse>> getAllObjects() {
        List<ObjectResponse> objects = contractQueryService.getAllObjects();
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/{objectId}")
    public ResponseEntity<ContractPageResponse> getContractPage(
            @PathVariable Long objectId,
            @RequestParam(required = false) Long selectedDetailId // 선택된 detailId(optional)
    ) {
        ContractPageResponse response = contractQueryService.getContractPage(objectId, selectedDetailId);
        return ResponseEntity.ok(response);
    }
}
