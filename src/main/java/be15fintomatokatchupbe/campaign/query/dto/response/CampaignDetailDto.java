package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
    // raw 타임스탬프 (쿼리 결과로 받음)
    private Timestamp startedAtRaw;
    private Timestamp endedAtRaw;
    // 포맷팅해서 클라이언트에 전달할 필드
    private String startedAt;
    private String endedAt;
    private List<Long> userList;                     // 고객사 담당자 유저 ID 리스트
    private String awarenessPath;
    private String productName;
    private Long productPrice;
    private Long expectedRevenue;                    // 예상 매출
    private BigDecimal expectedProfitMargin;         // 예상 이익률 (%)
    private String notes;
    private List<Long> categoryList;
}