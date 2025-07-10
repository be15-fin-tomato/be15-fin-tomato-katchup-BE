package be15fintomatokatchupbe.influencer.query.dto.request;

import lombok.Getter;
import lombok.Setter;
// import java.util.List; // List 타입은 더 이상 필요 없습니다.

@Getter
@Setter
public class InfluencerListRequestDTO {
    private Integer page = 0;
    private Integer size = 6;

    private String influencerName;

    // 카테고리 필드 제거
    // private String categories;

    private Long minSubscriber;
    private Long maxSubscriber;

    private Long minFollower;
    private Long maxFollower;

    private Long minPrice;
    private Long maxPrice;

    private String sortBy;
    private String sortOrder;

    public Integer getOffset() {
        if (this.page != null && this.size != null) {
            return this.page * this.size;
        }
        return 0;
    }
}
