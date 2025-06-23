package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DigitalContractListResponse {
    private List<DigitalContractDTO> detail;
}
