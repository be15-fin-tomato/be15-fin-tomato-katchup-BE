package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ContractSearchResponse {
    private final List<ContractCardResponse> response;
    private final Pagination pagination;
}
