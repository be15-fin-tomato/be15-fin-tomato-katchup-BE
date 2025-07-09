package be15fintomatokatchupbe.contract.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.contract.command.application.dto.request.*;
import be15fintomatokatchupbe.contract.command.application.service.ContractObjectCommandService;
import be15fintomatokatchupbe.contract.command.application.service.DetailCommandService;
import be15fintomatokatchupbe.utils.EmailUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name ="계약서")
@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractCommandController {

    private final DetailCommandService detailCommandService;
    private final ContractObjectCommandService contractObjectCommandService;
    private final EmailUtils emailUtils;

    @Operation(summary= "조건 생성" , description = "사용자는 계약서의 종류를 추가할 수 있다.")
    @PostMapping("/object/create")
    public ResponseEntity<ApiResponse<Void>> createObject(
            @RequestBody @Valid ObjectCreateRequest request) {
        contractObjectCommandService.createObject(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "계약서 생성", description = "사용자는 계약서의 내용과 관련 파일을 첨부하여 계약서을 생성할 수 있다.")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createDetail(
            @RequestPart("data") @Valid DetailCreateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        detailCommandService.createDetail(request, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "계약서 파일 전송", description = "사용자는 계약서와 관련된 첨부 파일을 전송할 수 있다.")
    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> sendEmail(
            @RequestPart("data") SendEmailRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        String content = request.getContent();
        String title = request.getTitle();
        String email = request.getTargetEmail();

        // 파일 포함 여부에 따라 메서드 분기 없이 하나로 처리 가능
        emailUtils.sendEmail(content, title, email, files);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "계약서 템플릿 조건 수정", description = "사용자는 계약서 템플릿의 조건을 수정할 수 있다.")
    @PatchMapping("/object/{objectId}")
    public ResponseEntity<ApiResponse<Void>> updateObject(
            @PathVariable Long objectId,
            @RequestBody ContractObjectUpdateRequest request) {

        contractObjectCommandService.updateObject(objectId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "계약서 템플릿 세부 조건 수정", description = "사용자는 계약서 템플릿의 세부 조건을 수정할 수 있다.")
    @PatchMapping(value = "/detail/{detailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateDetail(
            @PathVariable Long detailId,
            @RequestPart("data") DetailUpdateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        detailCommandService.updateDetail(detailId, request, file);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "계약서 템플릿 조건 삭제", description = "사용자는 계약서 템플릿의 조건을 삭제할 수 있다.")
    @DeleteMapping("/delete/object/{objectId}")
    public ResponseEntity<ApiResponse<Void>> deleteObject(@PathVariable Long objectId) {
        contractObjectCommandService.deleteObject(objectId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "계약서 템플릿 세부 조건 삭제", description = "사용자는 계약서 템플릿의 세부 조건을 삭제할 수 있다.")
    @DeleteMapping("/delete/detail/{detailId}")
    public ResponseEntity<ApiResponse<Void>> deleteDetail(@PathVariable Long detailId) {
        detailCommandService.deleteDetail(detailId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}

