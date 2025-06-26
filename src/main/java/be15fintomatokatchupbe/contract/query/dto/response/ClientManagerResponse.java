package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientManagerResponse {
    private Long id;
    private String name;
    private String email;
}

