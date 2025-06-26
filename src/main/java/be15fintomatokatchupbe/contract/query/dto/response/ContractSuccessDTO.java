package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContractSuccessDTO {

    private Long contractId; // 계약서 contract

    private String campaignName; // 캠페인명 contract-> campaign

    private String productName; // 상품명 campaign

    private String clientCompanyName; // 고객사명 campaign.client_company_id ->client_company

    private String influencerName; // influencer

    private String updatedAt; // 수정일 -> 계약서

    private Long fileId; // 파일아이디 contract_file

    private String originalName; // 파일

    private String filePath; // 파일

    private String program; // 파일
}
