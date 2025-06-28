package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.application.dto.request.DetailCreateRequest;
import be15fintomatokatchupbe.contract.command.application.dto.request.DetailUpdateRequest;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import be15fintomatokatchupbe.contract.command.domain.entity.Detail;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.DetailRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DetailCommandService {

    private final DetailRepository detailRepository;
    private final ContractFileRepository contractFileRepository;

    private static final String FILE_UPLOAD_DIR = "C:/Users/Playdata/Desktop/Tomato_contract_file/";

    @Transactional
    public void updateDetail(Long detailId, DetailUpdateRequest request, MultipartFile file) {
        Detail detail = detailRepository.findById(detailId)
                .orElseThrow(() -> new BusinessException(ContractErrorCode.NOT_FOUND));

        if (request.getSubTitle() != null) {
            detail.setSubTitle(request.getSubTitle());
        }

        if (request.getContent() != null) {
            detail.setContent(request.getContent());
        }

        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(FILE_UPLOAD_DIR, fileName);
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }

            String extension = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(f.lastIndexOf('.') + 1))
                    .orElse("unknown");

            ContractFile contractFile = ContractFile.builder()
                    .originalName(file.getOriginalFilename())
                    .filePath(dest.getAbsolutePath())
                    .program(extension)
                    .build();

            ContractFile savedFile = contractFileRepository.save(contractFile);
            detail.setFileId(savedFile.getFileId());
        }

        detail.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void createDetail(DetailCreateRequest request, MultipartFile file) {
        Long fileId = null;

        if (file != null && !file.isEmpty()) {
            try {
                // 원본 파일명 및 확장자 추출
                String originalFilename = file.getOriginalFilename();
                String extension = Optional.ofNullable(originalFilename)
                        .filter(f -> f.contains("."))
                        .map(f -> f.substring(originalFilename.lastIndexOf(".") + 1))
                        .orElse("");

                // 저장할 파일명 생성
                String storedFileName = UUID.randomUUID() + "." + extension;
                String fullPath = FILE_UPLOAD_DIR + storedFileName;

                // 실제 파일 저장
                File destination = new File(fullPath);
                file.transferTo(destination);

                // program 추정 (예: docx, hwp, txt 등)
                String program = switch (extension.toLowerCase()) {
                    case "doc", "docx" -> "word";
                    case "hwp" -> "hwp";
                    case "txt" -> "text";
                    case "pdf" -> "pdf";
                    default -> "unknown";
                };

                // DB 저장
                ContractFile savedFile = contractFileRepository.save(
                        ContractFile.builder()
                                .originalName(originalFilename)
                                .filePath(fullPath)
                                .program(program)
                                .build()
                );

                fileId = savedFile.getFileId();
            } catch (IOException e) {
                throw new BusinessException(ContractErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        // Detail 저장
        Detail detail = Detail.builder()
                .objectId(request.getObjectId())
                .subTitle(request.getSubTitle())
                .content(request.getContent())
                .fileId(fileId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        detailRepository.save(detail);
    }

    @Transactional
    public void deleteDetail(Long detailId) {
        Detail detail = detailRepository.findById(detailId)
                .orElseThrow(() -> new BusinessException(ContractErrorCode.NOT_FOUND));

        Long fileId = detail.getFileId();

        detailRepository.delete(detail);

        // 그 다음 ContractFile 삭제
        if (fileId != null) {
            contractFileRepository.findById(fileId).ifPresent(file -> {
                new File(file.getFilePath()).delete();
                contractFileRepository.delete(file);
            });
        }
    }
}

