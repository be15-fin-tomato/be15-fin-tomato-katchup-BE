package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DigitalContractDTO {
    private Long digitalContractId;
    private String template;
}
