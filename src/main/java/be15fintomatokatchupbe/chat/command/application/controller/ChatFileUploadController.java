package be15fintomatokatchupbe.chat.command.application.controller;

import be15fintomatokatchupbe.chat.exception.ChatErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Tag(name = "채팅방")
@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatFileUploadController {

    private final String uploadDir = "C:\\Users\\Playdata\\Desktop\\Tomato_contract_file";

    @Operation(summary = "채팅방 파일 업로드", description = "사용자는 채팅방에서 파일을 업로드 할 수 있다.")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new BusinessException(ChatErrorCode.UPLOAD_FAIL);
        }

        try {
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String savedFileName = UUID.randomUUID() + "_" + originalFilename;
            File destination = new File(uploadDir, savedFileName);

            // 경로 없으면 생성
            if (!destination.getParentFile().exists()) {
                destination.getParentFile().mkdirs();
            }

            file.transferTo(destination);

            // 클라이언트는 이 문자열을 그대로 fileUrl로 사용함
            String fileUrl = "local://" + savedFileName;

            return ResponseEntity.ok(fileUrl);

        } catch (Exception e) {
            log.error("파일 저장 중 오류 발생", e);
            throw new BusinessException(ChatErrorCode.UPLOAD_FAIL);
        }
    }
}
