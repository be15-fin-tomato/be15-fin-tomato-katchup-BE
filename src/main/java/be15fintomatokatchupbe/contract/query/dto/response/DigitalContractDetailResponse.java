package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DigitalContractDetailResponse {
    private Long digitalContractId;
    private String template;
    private LocalDateTime createdAt;
    private String content;
    private boolean isUsed;
}
