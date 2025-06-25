package be15fintomatokatchupbe.influencer.command.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileDTO {
    private String fileName;
    private String fileKey;
}
