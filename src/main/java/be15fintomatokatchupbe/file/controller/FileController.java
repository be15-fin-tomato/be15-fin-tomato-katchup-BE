package be15fintomatokatchupbe.file.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.domain.File;
import be15fintomatokatchupbe.file.dto.FileDownloadResult;
import be15fintomatokatchupbe.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.util.List;


@Tag(name = "파일")
@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<List<File>>> uploadFiles(@RequestPart List<MultipartFile> files) throws Exception {
        List<File> uploaded = fileService.uploadFile(files); // S3 업로드

        return ResponseEntity.ok(ApiResponse.success(uploaded));
    }

    @Operation(summary = "파일 다운로드", description = "사용자는 파일을 다운로드 할 수 있다.")
    @GetMapping("/download")
    public ResponseEntity<byte[]> streamFile(@RequestParam String key) {
        FileDownloadResult result = fileService.downloadFile(key);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + result.getOriginalFilename() + "\"")
                .header("Content-Type", result.getMimeType())
                .contentLength(result.getFileBytes().length)
                .body(result.getFileBytes());
    }
}
