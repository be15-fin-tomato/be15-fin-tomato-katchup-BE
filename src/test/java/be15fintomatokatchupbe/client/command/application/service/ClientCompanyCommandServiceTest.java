package be15fintomatokatchupbe.client.command.application.service;

import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientCompanyRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientManagerRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.UpdateClientCompanyRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.UpdateClientManagerRequest;
import be15fintomatokatchupbe.client.command.domain.aggregate.*;
import be15fintomatokatchupbe.client.command.domain.repository.*;
import be15fintomatokatchupbe.client.command.exception.ClientErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.relation.service.ClientCompanyUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientCompanyCommandServiceTest {

    @InjectMocks
    private ClientCompanyCommandService service;

    @Mock
    private ClientCompanyRepository clientCompanyRepository;
    @Mock
    private ClientManagerRepository clientManagerRepository;
    @Mock
    private ClientCompanyStatusRepository clientCompanyStatusRepository;
    @Mock
    private ClientManagerStatusRepository clientManagerStatusRepository;
    @Mock
    private ClientCompanyUserService clientCompanyUserService;
    @Mock
    private UserHelperService userHelperService;

    private ClientCompanyStatus companyStatus;
    private ClientManagerStatus managerStatus;

    @BeforeEach
    void setup() {
        companyStatus = ClientCompanyStatus.builder().clientCompanyStatusId(1L).statusName("활성").build();
        managerStatus = ClientManagerStatus.builder().clientManagerStatusId(1L).statusName("재직중").build();
    }

    @Test
    void createClientCompany_success() {
        CreateClientManagerRequest managerReq = CreateClientManagerRequest.builder()
                .name("홍길동")
                .clientManagerStatusId(1L)
                .build();

        CreateClientCompanyRequest request = CreateClientCompanyRequest.builder()
                .clientCompanyName("테스트회사")
                .clientCompanyStatusId(1L)
                .clientManagers(List.of(managerReq))
                .userIds(List.of(10L))
                .build();

        User user = User.builder().userId(10L).build();

        when(clientCompanyStatusRepository.findById(1L)).thenReturn(Optional.of(companyStatus));
        when(clientManagerStatusRepository.findById(1L)).thenReturn(Optional.of(managerStatus));
        when(userHelperService.findValidUserList(List.of(10L))).thenReturn(List.of(user));

        service.createClientCompany(request);

        verify(clientCompanyRepository, times(1)).save(any(ClientCompany.class));
        verify(clientCompanyUserService).createRelations(any(ClientCompany.class), eq(List.of(user)));
    }

    @Test
    void createClientCompany_invalidStatus_throwsException() {
        CreateClientCompanyRequest request = CreateClientCompanyRequest.builder()
                .clientCompanyName("잘못된회사")
                .clientCompanyStatusId(99L)
                .build();

        when(clientCompanyStatusRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createClientCompany(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ClientErrorCode.INVALID_STATUS.getMessage());
    }

    @Test
    void updateClientCompany_success() {
        ClientCompany existing = ClientCompany.builder()
                .clientCompanyId(1L)
                .clientCompanyName("기존회사")
                .clientCompanyStatus(companyStatus)
                .isDeleted(StatusType.N)
                .build();

        UpdateClientManagerRequest updateManagerReq = UpdateClientManagerRequest.builder()
                .name("이몽룡")
                .clientManagerStatusId(1L)
                .build();

        UpdateClientCompanyRequest request = UpdateClientCompanyRequest.builder()
                .clientCompanyName("변경된회사")
                .clientCompanyStatusId(1L)
                .clientManagers(List.of(updateManagerReq))
                .userIds(List.of(1L))
                .build();

        User user = User.builder().userId(1L).build();

        when(clientCompanyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientCompanyStatusRepository.findById(1L)).thenReturn(Optional.of(companyStatus));
        when(clientManagerStatusRepository.findById(1L)).thenReturn(Optional.of(managerStatus));
        when(userHelperService.findValidUserList(List.of(1L))).thenReturn(List.of(user));

        service.updateClientCompany(1L, request);

        verify(clientCompanyUserService).deleteByClientCompany(eq(existing));
        verify(clientCompanyUserService).createRelations(eq(existing), eq(List.of(user)));
        verify(clientCompanyRepository).save(eq(existing));
    }

    @Test
    void deleteClientCompany_success() {
        ClientManager m = ClientManager.builder().clientManagerId(2L).isDeleted(StatusType.N).build();
        ClientCompany company = ClientCompany.builder()
                .clientCompanyId(1L)
                .clientCompanyName("삭제회사")
                .clientCompanyStatus(companyStatus)
                .isDeleted(StatusType.N)
                .clientManagers(List.of(m))
                .build();

        when(clientCompanyRepository.findById(1L)).thenReturn(Optional.of(company));

        service.deleteClientCompany(1L);

        assertThat(company.getIsDeleted()).isEqualTo(StatusType.Y);
        assertThat(company.getDeletedAt()).isNotNull();
        assertThat(company.getClientManagers().get(0).getIsDeleted()).isEqualTo(StatusType.Y);
        verify(clientCompanyUserService).deleteByClientCompany(company);
        verify(clientCompanyRepository).save(company);
    }
}
