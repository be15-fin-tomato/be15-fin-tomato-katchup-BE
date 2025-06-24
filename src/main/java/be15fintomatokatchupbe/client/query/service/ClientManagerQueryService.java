package be15fintomatokatchupbe.client.query.service;

import be15fintomatokatchupbe.client.query.dto.ClientManagerSimpleResponse;
import be15fintomatokatchupbe.client.query.mapper.ClientCompanyQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientManagerQueryService {

    private final ClientCompanyQueryMapper clientCompanyQueryMapper;

    @Transactional(readOnly = true)
    public List<ClientManagerSimpleResponse> getManagersByClientCompanyId(Long clientCompanyId) {
        return clientCompanyQueryMapper.findManagersByClientCompanyId(clientCompanyId);
    }
}