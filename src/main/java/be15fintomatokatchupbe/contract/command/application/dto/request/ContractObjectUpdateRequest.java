package be15fintomatokatchupbe.contract.command.application.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractObjectUpdateRequest {
    private String title;
}
