package be15fintomatokatchupbe.client.query.service;

import be15fintomatokatchupbe.client.query.dto.*;
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

    public ClientCompanyListPagedResponse getClientCompanyList(int page, int size, ClientCompanySearchCondition condition) {
        int offset = (page - 1) * size;
        List<ClientCompanyListResponse> content =
                clientCompanyQueryMapper.searchClientCompanies(condition, offset, size);
        int totalCount = clientCompanyQueryMapper.countClientCompaniesByCondition(condition);
        int totalPage = (int) Math.ceil((double) totalCount / size);

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .size(size)
                .build();

        return new ClientCompanyListPagedResponse(content, pagination);
    }

    public ClientCompanySearchResponse findClientCompanyList(String keyword) {
        List<ClientCompanySimpleDto> clientCompanyList = clientCompanyQueryMapper.searchClientCompanyList(keyword);

        return ClientCompanySearchResponse.builder()
                .clientCompanyList(clientCompanyList)
                .build();
    }
}