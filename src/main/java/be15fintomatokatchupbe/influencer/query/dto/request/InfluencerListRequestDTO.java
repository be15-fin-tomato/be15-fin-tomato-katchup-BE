package be15fintomatokatchupbe.influencer.query.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfluencerListRequestDTO {
    private Integer page = 0;
    private Integer size = 6;

    private String influencerName;


    private Long minSubscriber;
    private Long maxSubscriber;

    private Long minFollower;
    private Long maxFollower;

    private Long minPrice;
    private Long maxPrice;

    private String sortBy;
    private String sortOrder;
    private List<Long> categoryIds;

    public Integer getOffset() {
        if (this.page != null && this.size != null) {
            return this.page * this.size;
        }
        return 0;
    }
}
