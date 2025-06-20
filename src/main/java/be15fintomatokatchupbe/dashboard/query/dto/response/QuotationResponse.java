package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuotationResponse {
    private String companyName;
    private String quotationName;
    private String expectedProfitMargin;
    private LocalDateTime createdAt;
}
