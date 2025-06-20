package be15fintomatokatchupbe.campaign.command.application.dto.request;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Campaign;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
public class CreateChanceRequest {
    private String campaignName;
    private Long campaignStatusId;
    private Long clientCompanyId;
    private Long clientManagerId;
    private LocalDateTime startedAt;
    private LocalDateTime  endedAt;
    private List<Integer> userList;
    private String awarenessPath;
    private String productName;
    private Long productPrice;
    private Long expectedRevenue;
    private BigDecimal expectedProfitMargin;
    private String notes;
    private List<Integer> categoryList;
}

