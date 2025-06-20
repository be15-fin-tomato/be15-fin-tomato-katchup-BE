package be15fintomatokatchupbe.client.command.application.service;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.client.command.domain.repository.ClientCompanyRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerRepository;
import be15fintomatokatchupbe.common.domain.StatusType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class ClientCommandService {
    private final ClientCompanyRepository clientCompanyRepository;
    private final ClientManagerRepository clientManagerRepository;

    /* 로직 작석용 */



    /* 레포지토리 참조용*/
    public ClientCompany findValidClientCompany(Long id){
        // TODO 에러코드 넣기
        return clientCompanyRepository.findByClientCompanyIdAndIsDeleted(id, StatusType.N)
                .orElseThrow(() -> new RuntimeException("존재X"));
    }

    public ClientManager finalValidClientManager(Long id){
        return clientManagerRepository.findByClientManagerIdAndIsDeleted(id, StatusType.N)
                .orElseThrow(() -> new RuntimeException("존재X"));
    }
}
