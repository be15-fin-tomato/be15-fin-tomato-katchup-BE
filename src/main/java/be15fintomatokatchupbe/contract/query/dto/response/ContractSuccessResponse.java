package be15fintomatokatchupbe.contract.query.dto.response;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ContractSuccessResponse {
    private List<ContractSuccessDTO> contractSuccess;
    private Pagination pagination;
}
