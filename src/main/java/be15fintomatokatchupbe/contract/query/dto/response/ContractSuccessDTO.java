package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContractSuccessDTO {

    private Long contractId; // 계약서

    private String campaignName; // 캠페인명

    private String productName; // 상품명

    private String clientCompanyName; // 고객사명

    private String influencerName; // 인플루언서명

    private String updatedAt; // 수정일 -> 계약서

    private Long fileId; // 파일아이디

    private String originalName; // 파일

    private String filePath; // 파일

    private String program; // 파일
}
