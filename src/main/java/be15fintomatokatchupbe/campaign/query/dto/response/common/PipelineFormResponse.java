package be15fintomatokatchupbe.campaign.query.dto.response.common;

import be15fintomatokatchupbe.campaign.query.dto.response.UserInfo;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@Getter
public class PipelineFormResponse {
    /* 파이프라인 제목 */
    private final String name;

    /* 고객사 */
    private final Long clientCompanyId;
    private final String clientCompanyName;

    /* 광고 담당자 */
    private final Long clientManagerId;
    private final String clientManagerName;

    /* 담당자 */
    private final List<UserInfo> userList;

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

    /* 내용 */
    private final String notes;
    private final String content;
}
