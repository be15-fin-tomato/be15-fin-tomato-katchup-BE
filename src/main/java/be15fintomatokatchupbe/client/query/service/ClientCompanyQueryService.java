package be15fintomatokatchupbe.client.query.service;

import be15fintomatokatchupbe.client.query.dto.ClientCompanyDetailResponse;
import be15fintomatokatchupbe.client.query.mapper.ClientCompanyQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientCompanyQueryService {

    private final ClientCompanyQueryMapper queryMapper;

    public ClientCompanyDetailResponse getDetail(Long clientCompanyId) {
        return queryMapper.findClientCompanyDetailById(clientCompanyId)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
