package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class ListupSearchResponse {
    private final List<ListupCardResponse> response;
    private final Pagination pagination;
}
