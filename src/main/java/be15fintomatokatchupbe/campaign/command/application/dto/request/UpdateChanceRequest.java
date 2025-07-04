package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateChanceRequest {
    private Long campaignId;
    private String campaignName;
    private Long campaignStatusId;
    private Long clientCompanyId;
    private Long clientManagerId;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private List<Long> userList;
    private String awarenessPath;
    private String productName;
    private Long productPrice;
    private Long expectedRevenue;
    private BigDecimal expectedProfitMargin;
    private String notes;
    private List<Long> categoryList;
}
