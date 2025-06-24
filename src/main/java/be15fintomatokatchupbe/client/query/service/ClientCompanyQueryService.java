package be15fintomatokatchupbe.client.query.service;

import be15fintomatokatchupbe.client.query.dto.ClientCompanyDetailResponse;
import be15fintomatokatchupbe.client.query.mapper.ClientCompanyQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientCompanyQueryService {

    private final ClientCompanyQueryMapper clientCompanyQueryMapper;

    public ClientCompanyDetailResponse getClientCompanyDetail(Long clientCompanyId) {
        return clientCompanyQueryMapper.findClientCompanyDetailById(clientCompanyId);
    }
}