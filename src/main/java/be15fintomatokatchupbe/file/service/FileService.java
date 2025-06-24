package be15fintomatokatchupbe.file.service;

import be15fintomatokatchupbe.file.domain.File;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.dto.FileDownloadResult;
import be15fintomatokatchupbe.file.exception.FileErrorCode;
import be15fintomatokatchupbe.file.repository.FileRepository;
import be15fintomatokatchupbe.influencer.command.application.dto.FileDTO;
import be15fintomatokatchupbe.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    private static final String CLOUD_FRONT_DOMAIN = "https://d152i3f1t56z95.cloudfront.net/";

    private final S3Client s3Client;
    private final FileUtil fileUtil;


    private final String SAVE_FILE_DIR = "file";
    private final String SAVE_IMAGE_DIR= "img";
    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;

    public List<File> uploadFile(List<MultipartFile> files) throws Exception {
        List<File> uploadedFiles = new ArrayList<>();
        log.info(bucketName);

        for (MultipartFile file : files) {
            log.info("파일 위조 확인: {}", fileUtil.validateFile(file));

            if (!fileUtil.validateFile(file)) {
                throw new BusinessException(FileErrorCode.FILE_FORMAT_ERROR);
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new BusinessException(FileErrorCode.FILE_TOO_BIG);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = fileUtil.getExtension(originalFilename).toLowerCase();
            String uuidFilename = UUID.randomUUID() + "." + extension;

            String mimeType = fileUtil.detectMimeType(file);
            boolean isImage = mimeType.startsWith("image/");
            String dir = isImage ? SAVE_IMAGE_DIR : SAVE_FILE_DIR;

            String fileKey = dir + "/" + uuidFilename;

            Map<String, String> metadata = new HashMap<>();
            metadata.put("mime-type", mimeType);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .metadata(metadata)
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            File fileEntity = File.builder()
                    .fileName(originalFilename)
                    .fileKey(fileKey)
                    .mimeType(mimeType)
                    .build();

            fileRepository.save(fileEntity);
            uploadedFiles.add(fileEntity);
        }

        return uploadedFiles;
    }

    public FileDownloadResult downloadFile(String key) {
        File fileInfo = fileRepository.findByFileKey(key)
                .orElseThrow(() -> new BusinessException(FileErrorCode.FILE_NOT_FOUND));

        try {
            var getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            var s3Object = s3Client.getObject(getObjectRequest);
            byte[] fileBytes = s3Object.readAllBytes();

            return FileDownloadResult.builder()
                    .fileBytes(fileBytes)
                    .originalFilename(fileInfo.getFileName())
                    .mimeType(fileInfo.getMimeType())
                    .build();

        } catch (IOException e) {
            log.error("파일 다운로드 중 오류 발생", e);
            throw new BusinessException(FileErrorCode.FILE_DOWNLOAD_ERROR); // 새 에러코드 정의 필요
        }
    }
}
