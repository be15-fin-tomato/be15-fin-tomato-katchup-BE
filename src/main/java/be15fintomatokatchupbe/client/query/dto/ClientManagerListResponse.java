package be15fintomatokatchupbe.client.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ClientManagerListResponse {
    public List<ClientManagerSearchDto> clientManagerList;
}
