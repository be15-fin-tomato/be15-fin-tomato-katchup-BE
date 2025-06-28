package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ContractViewResponse {
    private List<ContractViewDTO> contractView;
}
