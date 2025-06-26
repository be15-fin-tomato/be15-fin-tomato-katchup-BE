package be15fintomatokatchupbe.client.query.service;

import be15fintomatokatchupbe.client.query.dto.ClientCompanyDetailResponse;
import be15fintomatokatchupbe.client.query.dto.ClientCompanyListPagedResponse;
import be15fintomatokatchupbe.client.query.dto.ClientCompanyListResponse;
import be15fintomatokatchupbe.client.query.dto.ClientCompanyUserResponse;
import be15fintomatokatchupbe.client.query.mapper.ClientCompanyQueryMapper;
import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientCompanyQueryService {

    private final ClientCompanyQueryMapper clientCompanyQueryMapper;

    public ClientCompanyDetailResponse getClientCompanyDetail(Long clientCompanyId) {
        return clientCompanyQueryMapper.findClientCompanyDetailById(clientCompanyId);
    }

    public List<ClientCompanyUserResponse> getUsersByClientCompanyId(Long clientCompanyId) {
        return clientCompanyQueryMapper.findUsersByClientCompanyId(clientCompanyId);
    }

    public ClientCompanyListPagedResponse getClientCompanyList(int page, int size) {
        int offset = (page - 1) * size;
        List<ClientCompanyListResponse> content = clientCompanyQueryMapper.findClientCompanyList(offset, size);
        int totalCount = clientCompanyQueryMapper.countClientCompanies();
        int totalPage = (int) Math.ceil((double) totalCount / size);

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .size(size)
                .build();

        return new ClientCompanyListPagedResponse(content, pagination);
    }
}