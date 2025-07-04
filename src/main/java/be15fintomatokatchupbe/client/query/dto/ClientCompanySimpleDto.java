package be15fintomatokatchupbe.client.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClientCompanySimpleDto {
    public Long id;
    public String name;
}
