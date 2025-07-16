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
    private String campaignName; // 캠페인명
    private Long campaignStatusId; // 캠페인 상태
    private Long clientCompanyId; // 회사
    private Long clientManagerId; // 사원
    private String awarenessPath; // 인지경로
    private String productName; // 상품명
    private Long productPrice; // 상품가격
    private Long expectedRevenue; // 예상매출
    private BigDecimal expectedProfitMargin; // 예상이익률
    private String notes; // 비고
    private LocalDate startedAt; // 시작인
    private LocalDate endedAt; // 마감일
    private List<Long> categoryList; // 카테고리
}
