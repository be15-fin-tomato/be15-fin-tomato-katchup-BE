package be15fintomatokatchupbe.contract.command.application.dto.request;

import lombok.Data;

@Data
public class DetailCreateRequest {
    private Long objectId;
    private String subTitle;
    private String content;
}

