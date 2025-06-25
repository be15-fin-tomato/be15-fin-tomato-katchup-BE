package be15fintomatokatchupbe.campaign.query.dto.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class ContractFormDTO {
    /* 파이프라인 제목 */
    private final String name;

    /* 고객사 */
    private final Long clientCompanyId;
    private final String clientCompanyName;

    /* 광고 담당자 */
    private final Long clientManagerId;
    private final String clientManagerName;

    /* 캠페인 정보*/
    private final Long campaignId;
    private final String campaignName;

    /* 파이프라인 상태*/
    private final Long pipelineStatusId;
    private final String pipelineStatusName;

    /* 날짜들*/
    private final LocalDate requestAt;
    private final LocalDate presentAt;
    private final LocalDate startedAt;
    private final LocalDate endedAt;
    private final String notes;
    private final String content;

    /* 견적용 */
    private final Long expectedRevenue;
    private final Long availableQuantity;
    private final Long expectedProfit;
}
