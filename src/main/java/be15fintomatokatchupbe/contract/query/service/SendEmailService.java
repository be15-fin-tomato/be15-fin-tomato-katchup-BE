package be15fintomatokatchupbe.contract.query.service;

import be15fintomatokatchupbe.client.query.mapper.ClientManagerQueryMapper;
import be15fintomatokatchupbe.contract.query.dto.request.ClientManagerSearchRequest;
import be15fintomatokatchupbe.contract.query.dto.response.ClientManagerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final ClientManagerQueryMapper clientManagerQueryMapper;

    public List<ClientManagerResponse> getManagersWithStatus(ClientManagerSearchRequest request) {
        return clientManagerQueryMapper.findManagersByCondition(
                request.getName(),
                request.getEmail()
        );
    }
}

