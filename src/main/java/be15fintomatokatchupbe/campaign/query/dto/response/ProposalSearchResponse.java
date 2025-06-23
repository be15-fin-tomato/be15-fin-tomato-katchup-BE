package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProposalSearchResponse {
    private final List<ProposalCardResponse> response;
    private final Pagination pagination;
}
