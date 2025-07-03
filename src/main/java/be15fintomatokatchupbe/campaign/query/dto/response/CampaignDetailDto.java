package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CampaignDetailDto {
    private Long campaignId;
    private String campaignName;
    private Long campaignStatusId;
    private Long clientCompanyId;
    private String clientCompanyName;
    private Long clientManagerId;
    private String clientManagerName;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private List<Long> userList;                     // 고객사 담당자 유저 ID 리스트
    private String awarenessPath;
    private String productName;
    private Long productPrice;
    private Long expectedRevenue;                    // 예상 매출
    private BigDecimal expectedProfitMargin;         // 예상 이익률 (%)
    private String notes;
    private List<Long> categoryList;
}