package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DetailInfoResponse {
    private Long detailId;
    private String subTitle;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private FileInfo file;

    @Getter
    @NoArgsConstructor
    public static class FileInfo {
        private Long fileId;
        private String originalName;
        private String filePath;
        private String program;
    }
}