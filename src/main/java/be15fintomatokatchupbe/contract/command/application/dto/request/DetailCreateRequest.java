package be15fintomatokatchupbe.contract.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailCreateRequest {
    private Long objectId;
    private String subTitle;
    private String content;
}

