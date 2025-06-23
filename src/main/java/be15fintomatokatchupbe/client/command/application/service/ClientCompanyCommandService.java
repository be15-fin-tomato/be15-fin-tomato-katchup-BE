package be15fintomatokatchupbe.client.command.application.service;

import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientCompanyRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientManagerRequest;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompanyStatus;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManagerStatus;
import be15fintomatokatchupbe.client.command.domain.repository.ClientCompanyRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientCompanyStatusRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerStatusRepository;
import be15fintomatokatchupbe.client.command.exception.ClientErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientCompanyCommandService {

    private final ClientCompanyRepository clientCompanyRepository;
    private final ClientManagerRepository clientManagerRepository;
    private final ClientCompanyStatusRepository clientCompanyStatusRepository;
    private final ClientManagerStatusRepository clientManagerStatusRepository;

    @Transactional
    public void createClientCompany(CreateClientCompanyRequest request) {
        ClientCompanyStatus status = clientCompanyStatusRepository.findById(request.getClientCompanyStatusId())
                .orElseThrow(() -> new BusinessException(ClientErrorCode.INVALID_STATUS));

        ClientCompany company = ClientCompany.builder()
                .clientCompanyName(request.getClientCompanyName())
                .clientCompanyStatus(status)
                .businessId(request.getBusinessId())
                .sales(request.getSales())
                .numberOfEmployees(request.getNumberOfEmployees())
                .telephone(request.getTelephone())
                .fax(request.getFax())
                .address(request.getAddress())
                .detailAddress(request.getDetailAddress())
                .notes(request.getNotes())
                .build();

        for (CreateClientManagerRequest managerRequest : request.getClientManagers()) {
            ClientManagerStatus managerStatus = clientManagerStatusRepository.findById(managerRequest.getClientManagerStatusId())
                    .orElseThrow(() -> new BusinessException(ClientErrorCode.INVALID_MANAGER_STATUS));

            ClientManager manager = ClientManager.builder()
                    .clientCompany(company)
                    .clientManagerStatus(managerStatus)
                    .name(managerRequest.getName())
                    .department(managerRequest.getDepartment())
                    .position(managerRequest.getPosition())
                    .phone(managerRequest.getPhone())
                    .telephone(managerRequest.getTelephone())
                    .email(managerRequest.getEmail())
                    .notes(managerRequest.getNotes())
                    .build();

            company.addManager(manager);
        }

        clientCompanyRepository.save(company);
    }
}