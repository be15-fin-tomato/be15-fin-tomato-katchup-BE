package be15fintomatokatchupbe.contract.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DigitalContractCreateRequest {
    private String template;
    private String content;
}
