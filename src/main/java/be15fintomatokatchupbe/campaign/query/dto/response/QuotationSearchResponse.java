package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class QuotationSearchResponse {
    private final List<QuotationCardResponse> response;
    private final Pagination pagination;
}
