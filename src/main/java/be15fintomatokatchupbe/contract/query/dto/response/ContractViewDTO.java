package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractViewDTO {
    private String originalName;

    private String filePath;

    private String program;
}
