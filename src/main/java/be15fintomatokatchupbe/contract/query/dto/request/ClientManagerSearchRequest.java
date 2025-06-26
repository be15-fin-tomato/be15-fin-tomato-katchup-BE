package be15fintomatokatchupbe.contract.query.dto.request;

import lombok.Data;

@Data
public class ClientManagerSearchRequest {
    private String name;
    private String email;
}
