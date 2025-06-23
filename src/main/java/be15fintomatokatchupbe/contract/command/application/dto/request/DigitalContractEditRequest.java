package be15fintomatokatchupbe.contract.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DigitalContractEditRequest {
    private Long digitalContractId;
    private String template;
    private String content;
}
