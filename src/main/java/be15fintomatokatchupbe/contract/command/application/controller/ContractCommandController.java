package be15fintomatokatchupbe.contract.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.contract.command.application.dto.request.ContractObjectUpdateRequest;
import be15fintomatokatchupbe.contract.command.application.dto.request.DetailCreateRequest;
import be15fintomatokatchupbe.contract.command.application.dto.request.DetailUpdateRequest;
import be15fintomatokatchupbe.contract.command.application.dto.request.SendEmailRequest;
import be15fintomatokatchupbe.contract.command.application.service.ContractObjectCommandService;
import be15fintomatokatchupbe.contract.command.application.service.DetailCommandService;
import be15fintomatokatchupbe.utils.EmailUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractCommandController {

    private final DetailCommandService detailCommandService;
    private final ContractObjectCommandService contractObjectCommandService;
    private final EmailUtils emailUtils;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createDetail(
            @RequestPart("data") @Valid DetailCreateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        detailCommandService.createDetail(request, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> sendEmail(
            @RequestPart("data") SendEmailRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        String content = request.getContent();
        String title = request.getTitle();
        String email = request.getTargetEmail();

        // 파일 포함 여부에 따라 메서드 분기 없이 하나로 처리 가능
        emailUtils.sendEmail(content, title, email, file);

        return ResponseEntity.ok(ApiResponse.success(null));
    }



    @PatchMapping("/object/{objectId}")
    public ResponseEntity<ApiResponse<Void>> updateObject(
            @PathVariable Long objectId,
            @RequestBody ContractObjectUpdateRequest request) {

        contractObjectCommandService.updateObject(objectId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping(value = "/detail/{detailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateDetail(
            @PathVariable Long detailId,
            @RequestPart("data") DetailUpdateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        detailCommandService.updateDetail(detailId, request, file);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/delete/object/{objectId}")
    public ResponseEntity<ApiResponse<Void>> deleteObject(@PathVariable Long objectId) {
        contractObjectCommandService.deleteObject(objectId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/delete/detail/{detailId}")
    public ResponseEntity<ApiResponse<Void>> deleteDetail(@PathVariable Long detailId) {
        detailCommandService.deleteDetail(detailId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}

