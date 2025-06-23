package be15fintomatokatchupbe.client.command.application.service;

import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientCompanyRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientManagerRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.UpdateClientCompanyRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.UpdateClientManagerRequest;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompanyStatus;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManagerStatus;
import be15fintomatokatchupbe.client.command.domain.repository.ClientCompanyRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientCompanyStatusRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerStatusRepository;
import be15fintomatokatchupbe.client.command.exception.ClientErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.relation.service.ClientCompanyUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientCompanyCommandService {

    private final ClientCompanyRepository clientCompanyRepository;
    private final ClientManagerRepository clientManagerRepository;
    private final ClientCompanyStatusRepository clientCompanyStatusRepository;
    private final ClientManagerStatusRepository clientManagerStatusRepository;
    private final ClientCompanyUserService clientCompanyUserService;
    private final UserHelperService userHelperService;

    // 1) 고객사 + 사원 + 담당자(유저) 동시 생성
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

        // 고객사 저장
        clientCompanyRepository.save(company);

        // 담당자 N:M 매핑 생성
        List<User> users = userHelperService.findValidUserList(request.getUserIds());
        clientCompanyUserService.createRelations(company, users);
    }

    // 2) 고객사 + 사원 + 담당자(유저) 수정
    @Transactional
    public void updateClientCompany(Long clientCompanyId, UpdateClientCompanyRequest request) {
        ClientCompany company = clientCompanyRepository.findById(clientCompanyId)
                .filter(c -> c.getIsDeleted() == StatusType.N)
                .orElseThrow(() -> new BusinessException(ClientErrorCode.NOT_FOUND));

        ClientCompanyStatus status = clientCompanyStatusRepository.findById(request.getClientCompanyStatusId())
                .orElseThrow(() -> new BusinessException(ClientErrorCode.INVALID_STATUS));

        company.update(
                request.getClientCompanyName(),
                status,
                request.getBusinessId(),
                request.getSales(),
                request.getNumberOfEmployees(),
                request.getTelephone(),
                request.getFax(),
                request.getAddress(),
                request.getDetailAddress(),
                request.getNotes()
        );

        // 기존 사원 Map으로 구성
        Map<Long, ClientManager> existingManagers = company.getClientManagers().stream()
                .filter(m -> m.getClientManagerId() != null)
                .collect(Collectors.toMap(ClientManager::getClientManagerId, m -> m));

        company.clearManagers();

        for (UpdateClientManagerRequest req : request.getClientManagers()) {
            ClientManagerStatus managerStatus = clientManagerStatusRepository.findById(req.getClientManagerStatusId())
                    .orElseThrow(() -> new BusinessException(ClientErrorCode.INVALID_MANAGER_STATUS));

            if (req.getClientManagerId() != null && existingManagers.containsKey(req.getClientManagerId())) {
                // 기존 사원 수정
                ClientManager m = existingManagers.get(req.getClientManagerId());
                m.update(req.getName(), req.getDepartment(), req.getPosition(), req.getPhone(), req.getTelephone(), req.getEmail(), req.getNotes(), managerStatus);
                company.addManager(m);
            } else {
                // 신규 추가
                ClientManager newManager = ClientManager.builder()
                        .clientCompany(company)
                        .clientManagerStatus(managerStatus)
                        .name(req.getName())
                        .department(req.getDepartment())
                        .position(req.getPosition())
                        .phone(req.getPhone())
                        .telephone(req.getTelephone())
                        .email(req.getEmail())
                        .notes(req.getNotes())
                        .build();
                company.addManager(newManager);
            }
        }

        // 담당자 매핑 hard delete 처리
        clientCompanyUserService.deleteByClientCompany(company);
        // 새 담당자 매핑 생성
        List<User> users = userHelperService.findValidUserList(request.getUserIds());
        clientCompanyUserService.createRelations(company, users);
        clientCompanyRepository.save(company);
    }
}