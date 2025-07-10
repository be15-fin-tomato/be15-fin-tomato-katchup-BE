package be15fintomatokatchupbe.client.query.dto;

import lombok.Getter;

import java.sql.Date;

@Getter
public class ClientCompanyContractDto {

    private Long contractId;

    private Long campaignId; // 캠페인 아이디 -> 성과대시보드로 가는 ID

    private String campaignName;  // 캠페인 명

    private String productName; // 상품명

    private String name; // 인플루언서 명

    private Long totalProfit; // 파이프라인 -> 총 수익

    private Date startedAt; // 시작된 날

    private Date endedAt; // 마지막 날




}
