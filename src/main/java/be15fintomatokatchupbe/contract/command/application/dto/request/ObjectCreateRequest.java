package be15fintomatokatchupbe.contract.command.application.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObjectCreateRequest {
    private String title;
}
