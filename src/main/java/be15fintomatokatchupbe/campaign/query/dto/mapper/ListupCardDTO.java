package be15fintomatokatchupbe.campaign.query.dto.mapper;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListupCardDTO {
    private Long pipelineId;    // 파이프라인 ID
    private String name;    // 파이프라인 이름
    private String userName;    // 유저 이름
    private String campaignName; //캠페인 명
    private String clientCompanyName;   // 고객사 명
//    private String clientManagerName;   // 광고 담당자명
    private String productName; // 상품 이름
//    private String userNameInfo;    // 담당자 명
    private String influencerNameInfo;  // 인플루언서 이름
}
