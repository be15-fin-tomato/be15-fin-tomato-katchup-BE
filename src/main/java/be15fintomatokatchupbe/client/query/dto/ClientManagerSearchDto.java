package be15fintomatokatchupbe.client.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientManagerSearchDto {
    public Long id;
    public String name;
    public String email;
}
