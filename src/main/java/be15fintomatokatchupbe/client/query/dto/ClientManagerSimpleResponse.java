package be15fintomatokatchupbe.client.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientManagerSimpleResponse {
    private Long clientCompanyId;
    private Long clientManagerId;
    private String name;
}
