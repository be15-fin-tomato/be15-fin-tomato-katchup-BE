package be15fintomatokatchupbe.contract.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailUpdateRequest {
    private String subTitle;
    private String content;
}
