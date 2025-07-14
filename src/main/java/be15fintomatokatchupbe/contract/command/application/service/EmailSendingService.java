package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.dto.FileDownloadResult;
import be15fintomatokatchupbe.file.service.FileService;
import be15fintomatokatchupbe.contract.command.application.dto.request.SendEmailRequest;
import be15fintomatokatchupbe.utils.EmailUtils;

// ContractFile 관련 임포트 추가
import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode; // 계약서 파일 관련 에러코드 임포트

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendingService {

    private final EmailUtils emailUtils;
    private final FileService fileService;
    private final ContractFileRepository contractFileRepository; // ContractFileRepository 주입

    public void sendContractEmail(SendEmailRequest request, List<MultipartFile> uploadedFiles) {
        String content = request.getContent();
        String title = request.getTitle();
        String targetEmail = request.getTargetEmail();
        Long fileId = request.getFileId(); // SendEmailRequest에서 fileId를 Long 타입으로 받음

        List<MultipartFile> allAttachments = new ArrayList<>();

        // 1. 프론트엔드에서 직접 업로드된 파일들을 첨부 목록에 추가
        if (uploadedFiles != null && !uploadedFiles.isEmpty()) {
            allAttachments.addAll(uploadedFiles);
            log.info("로컬에서 업로드된 파일 {}개 첨부됨.", uploadedFiles.size());
        }

        // 2. fileId가 제공된 경우, ContractFile 정보를 통해 S3 파일 키를 조회하고 파일을 첨부
        if (fileId != null) {
            try {
                // fileId로 ContractFile 엔티티를 조회
                ContractFile contractFile = contractFileRepository.findById(fileId)
                        .orElseThrow(() -> new BusinessException(ContractErrorCode.NOT_FOUND)); // 적절한 에러코드 정의 필요

                String s3FileKey = contractFile.getFilePath(); // ContractFile 엔티티의 filePath가 S3 키
                String originalFileNameFromDb = contractFile.getOriginalName(); // ContractFile 엔티티의 originalName
                String mimeTypeFromDb = contractFile.getProgram(); // ContractFile 엔티티의 program이 MIME 타입

                if (s3FileKey != null && !s3FileKey.isEmpty()) {
                    // FileService를 통해 S3에서 파일의 byte[] 데이터와 메타데이터를 가져옴
                    FileDownloadResult s3FileResult = fileService.downloadContractFile(s3FileKey);
                    byte[] fileBytes = s3FileResult.getFileBytes();
                    // FileDownloadResult의 originalFilename과 mimeType을 사용할 수도 있지만, DB에서 가져온 값을 신뢰할 수도 있습니다.
                    // 이 예시에서는 DB 값을 사용했습니다. 필요에 따라 선택하세요.
                    // String originalFilename = s3FileResult.getOriginalFilename();
                    // String mimeType = s3FileResult.getMimeType();

                    // S3에서 가져온 파일의 실제 바이트 크기를 로그로 출력 (가장 중요)
                    log.info("S3에서 가져온 파일 바이트 크기 (Key: {}): {} 바이트", s3FileKey, fileBytes.length);

                    // S3에서 가져온 byte[] 데이터를 MockMultipartFile로 변환하여 첨부 목록에 추가
                    MultipartFile s3ConvertedMultipartFile = new MockMultipartFile(
                            "s3_attachment",
                            originalFileNameFromDb,
                            mimeTypeFromDb,
                            fileBytes
                    );
                    allAttachments.add(s3ConvertedMultipartFile);
                    log.info("S3 파일 (Key: {}) 첨부됨. 파일명: {}", s3FileKey, originalFileNameFromDb);

                } else {
                    log.warn("파일 ID {} 에 연결된 S3 파일 키(filePath)가 없습니다. S3 파일 첨부 없이 이메일 전송을 진행합니다.", fileId);
                }
            } catch (BusinessException e) {
                log.error("S3 파일 조회 또는 다운로드 중 비즈니스 오류 발생 (파일 ID: {}): {}", fileId, e.getMessage());
            } catch (Exception e) {
                log.error("S3 파일 처리 중 예상치 못한 오류 발생 (파일 ID: {}): {}", fileId, e.getMessage(), e);
            }
        } else {
            log.info("fileId가 제공되지 않았습니다. S3 파일 첨부 없이 로컬 파일만으로 이메일 전송을 진행합니다.");
        }

        // EmailUtils를 호출하여 모든 첨부 파일 (로컬 + S3)과 함께 이메일 전송
        emailUtils.sendEmail(content, title, targetEmail, allAttachments);
        log.info("이메일 전송 완료: To {}, Title: {}", targetEmail, title);
    }
}

/* 안된 이유
* */