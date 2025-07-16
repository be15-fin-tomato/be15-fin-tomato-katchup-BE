package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.application.dto.request.DetailCreateRequest;
import be15fintomatokatchupbe.contract.command.application.dto.request.DetailUpdateRequest;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import be15fintomatokatchupbe.contract.command.domain.entity.Detail;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.DetailRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import be15fintomatokatchupbe.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetailCommandService {

    private final DetailRepository detailRepository;
    private final ContractFileRepository contractFileRepository;
    private final FileService fileService;

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
            try {
                String originalFilename = file.getOriginalFilename();
                String extension = Optional.ofNullable(originalFilename)
                        .filter(f -> f.contains("."))
                        .map(f -> f.substring(originalFilename.lastIndexOf(".") + 1))
                        .orElse("unknown");

                String program = switch (extension.toLowerCase()) {
                    case "doc", "docx" -> "word";
                    case "hwp" -> "hwp";
                    case "txt" -> "text";
                    case "pdf" -> "pdf";
                    default -> "unknown";
                };

                List<ContractFile> uploadedFiles = fileService.uploadContractFile(List.of(file));
                ContractFile s3File = uploadedFiles.get(0);

                ContractFile savedFile = contractFileRepository.save(
                        ContractFile.builder()
                                .originalName(originalFilename)
                                .filePath(s3File.getFilePath())  // S3 key 또는 URL
                                .program(program)
                                .build()
                );
                log.info(String.valueOf(savedFile.getFileId()));

                detail.setFileId(savedFile.getFileId());
                detailRepository.save(detail);

            } catch (BusinessException e) {
                if (e.getErrorCode() == ContractErrorCode.FILE_TOO_BIG) {
                    throw e;
                } else {
                    log.error("파일 업로드 중 비즈니스 오류 발생 (FILE_UPLOAD_FAILED): {}", e.getMessage(), e);
                    throw new BusinessException(ContractErrorCode.FILE_UPLOAD_FAILED);
                }
            } catch (Exception e) { // 그 외의 일반적인 예외 (IOException, RuntimeException 등)
                // S3 통신 오류, 알 수 없는 파일 시스템 오류 등
                log.error("파일 업로드 중 알 수 없는 오류 발생 (updateDetail): {}", e.getMessage(), e);
                throw new BusinessException(ContractErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        detail.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void createDetail(DetailCreateRequest request, MultipartFile file) {
        Long fileId = null;

        if (file != null && !file.isEmpty()) {
            try {
                // 1. 파일명 및 확장자
                String originalFilename = file.getOriginalFilename();
                String extension = Optional.ofNullable(originalFilename)
                        .filter(f -> f.contains("."))
                        .map(f -> f.substring(originalFilename.lastIndexOf(".") + 1))
                        .orElse("unknown");

                String program = switch (extension.toLowerCase()) {
                    case "doc", "docx" -> "word";
                    case "hwp" -> "hwp";
                    case "txt" -> "text";
                    case "pdf" -> "pdf";
                    default -> "unknown";
                };

                List<ContractFile> uploadedFiles = fileService.uploadContractFile(List.of(file));
                ContractFile s3File = uploadedFiles.get(0);

                ContractFile savedFile = contractFileRepository.save(
                        ContractFile.builder()
                                .originalName(originalFilename)
                                .filePath(s3File.getFilePath())
                                .program(program)
                                .build()
                );

                fileId = savedFile.getFileId();
            } catch (BusinessException e) {
                if (e.getErrorCode() == ContractErrorCode.FILE_TOO_BIG) {
                    throw e;
                } else {
                    log.error("파일 업로드 중 비즈니스 오류 발생 (FILE_UPLOAD_FAILED): {}", e.getMessage(), e);
                    throw new BusinessException(ContractErrorCode.FILE_UPLOAD_FAILED);
                }
            } catch (Exception e) { // 그 외의 일반적인 예외
                log.error("파일 업로드 중 알 수 없는 오류 발생 (createDetail): {}", e.getMessage(), e);
                throw new BusinessException(ContractErrorCode.FILE_UPLOAD_FAILED);
            }
        }

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

        if (fileId != null) {
            contractFileRepository.findById(fileId).ifPresent(file -> {
                contractFileRepository.delete(file);
            });
        }
    }
}