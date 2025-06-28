package be15fintomatokatchupbe.client.query.dto;

import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ClientCompanyListPagedResponse {
    private List<ClientCompanyListResponse> items;
    private Pagination pagination;
}
