package be15fintomatokatchupbe.client.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ClientCompanyContractResponse {
    List<ClientCompanyContractDto> clientCompanyContract;
}
