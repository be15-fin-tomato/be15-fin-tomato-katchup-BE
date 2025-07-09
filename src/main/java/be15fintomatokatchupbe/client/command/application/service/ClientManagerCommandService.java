package be15fintomatokatchupbe.client.command.application.service;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerRepository;
import be15fintomatokatchupbe.client.command.exception.ClientErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientManagerCommandService {

    private final ClientManagerRepository clientManagerRepository;

    @Transactional
    public void deleteClientManager(Long clientManagerId) {
        ClientManager manager = clientManagerRepository.findByClientManagerIdAndIsDeleted(clientManagerId, StatusType.N)
                .orElseThrow(() -> new BusinessException(ClientErrorCode.CLIENT_MANAGER_NOT_FOUND));

        manager.softDelete();
    }
}
