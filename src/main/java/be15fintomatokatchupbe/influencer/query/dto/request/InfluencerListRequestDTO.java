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

    public boolean isInitialQuery() {
        return (page == 0 || page == null) &&
                (size == 6 || size == null) &&
                (influencerName == null || influencerName.isBlank()) &&
                minSubscriber == null &&
                maxSubscriber == null &&
                minFollower == null &&
                maxFollower == null &&
                minPrice == null &&
                maxPrice == null &&
                (sortBy == null || sortBy.isBlank()) &&
                (sortOrder == null || sortOrder.isBlank()) &&
                (categoryIds == null || categoryIds.isEmpty());
    }

    public boolean isAiInitialQuery() {
        return (page == 0 || page == null) &&
                (size == 50 || size == null) &&
                (influencerName == null || influencerName.isBlank()) &&
                minSubscriber == null &&
                maxSubscriber == null &&
                minFollower == null &&
                maxFollower == null &&
                minPrice == null &&
                maxPrice == null &&
                (sortBy == null || sortBy.isBlank()) &&
                (sortOrder == null || sortOrder.isBlank()) &&
                (categoryIds == null || categoryIds.isEmpty());
    }
}
