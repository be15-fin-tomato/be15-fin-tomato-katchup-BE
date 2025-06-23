package be15fintomatokatchupbe.file.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileDownloadResult {
    private byte[] fileBytes;
    private String originalFilename;
    private String mimeType;
}