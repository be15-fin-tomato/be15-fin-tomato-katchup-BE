package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetailInfoResponse {
    private Long detailId;
    private String subTitle;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private FileInfo file;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private Long fileId;
        private String originalName;
        private String filePath;
        private String program;
    }
}