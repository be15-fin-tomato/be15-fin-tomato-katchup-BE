package be15fintomatokatchupbe.campaign.query.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class ContractListRequest {
    private String stepType; // "기회인지","리스트업","제안","견적", "협상", "계약","매출","사후관리"

    private Long campaignStatus; // 취소, 진행중, 보류, 게시대기, 완료

    private Long expectedRevenueRange ; // 예상금액 10000000, 10000001~30000000, 30000001~50000000, 50000001~70000000, 70000001~100000000, 100000001

    private Date startedDate; // 시작날짜
    private Date endedDate; // 끝날짜

    private String clientCompanyName; // 회사명
    private String clientManagerName; // 사원명
}
