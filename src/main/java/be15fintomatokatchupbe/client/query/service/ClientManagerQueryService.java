package be15fintomatokatchupbe.client.query.service;

import be15fintomatokatchupbe.client.query.dto.ClientManagerListResponse;
import be15fintomatokatchupbe.client.query.dto.ClientManagerSearchDto;
import be15fintomatokatchupbe.client.query.dto.ClientManagerSimpleResponse;
import be15fintomatokatchupbe.client.query.mapper.ClientCompanyQueryMapper;
import be15fintomatokatchupbe.client.query.mapper.ClientManagerQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientManagerQueryService {

    private final ClientCompanyQueryMapper clientCompanyQueryMapper;
    private final ClientManagerQueryMapper clientManagerQueryMapper;

    @Transactional(readOnly = true)
    public List<ClientManagerSimpleResponse> getManagersByClientCompanyId(Long clientCompanyId) {
        return clientCompanyQueryMapper.findManagersByClientCompanyId(clientCompanyId);
    }

    public ClientManagerListResponse getClientManagerList(String keyword, Long clientCompanyId) {
        List<ClientManagerSearchDto> clientManagerList = clientManagerQueryMapper.getClientManagerList(keyword, clientCompanyId);

        return ClientManagerListResponse.builder()
                .clientManagerList(clientManagerList)
                .build();
    }
}