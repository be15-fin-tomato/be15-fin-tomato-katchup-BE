package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RevenueSearchResponse {
    private final List<RevenueCardResponse> response;
    private final Pagination pagination;
}
