package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClientCompanyResponse {
    private String clientCompanyName;
    private String telephone;
    private LocalDateTime createdAt;
    private String statusName;
}
