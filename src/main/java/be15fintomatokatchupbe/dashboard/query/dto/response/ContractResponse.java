package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContractResponse {
    private String companyName;
    private String contractName;
    private String statusName;
    private LocalDateTime createdAt;
}
