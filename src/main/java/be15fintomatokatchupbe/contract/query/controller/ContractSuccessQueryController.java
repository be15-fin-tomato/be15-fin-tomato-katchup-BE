package be15fintomatokatchupbe.contract.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "계약 완료 페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractSuccessQueryController {

    @GetMapping("/success")
    public ResponseEntity<ApiResponse<>> getContractSuccess() {

    }
}
