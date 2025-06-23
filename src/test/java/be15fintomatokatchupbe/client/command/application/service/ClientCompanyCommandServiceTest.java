package be15fintomatokatchupbe.client.command.application.service;

import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientCompanyRequest;
import be15fintomatokatchupbe.client.command.application.dto.request.CreateClientManagerRequest;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompanyStatus;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManagerStatus;
import be15fintomatokatchupbe.client.command.domain.repository.ClientCompanyRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientCompanyStatusRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerRepository;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerStatusRepository;
import be15fintomatokatchupbe.client.command.exception.ClientErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientCompanyCommandServiceTest {

    private ClientCompanyRepository clientCompanyRepository;
    private ClientManagerRepository clientManagerRepository;
    private ClientCompanyStatusRepository clientCompanyStatusRepository;
    private ClientManagerStatusRepository clientManagerStatusRepository;

    private ClientCompanyCommandService sut;

    @BeforeEach
    void setUp() {
        clientCompanyRepository = mock(ClientCompanyRepository.class);
        clientManagerRepository = mock(ClientManagerRepository.class);
        clientCompanyStatusRepository = mock(ClientCompanyStatusRepository.class);
        clientManagerStatusRepository = mock(ClientManagerStatusRepository.class);

        sut = new ClientCompanyCommandService(
                clientCompanyRepository,
                clientManagerRepository,
                clientCompanyStatusRepository,
                clientManagerStatusRepository
        );
    }

    @Nested
    @DisplayName("고객사/사원 등록")
    class CreateClientCompany {

        @Test
        @DisplayName("정상 등록!")
        void success() {
            // ===== given =====
            CreateClientManagerRequest mReq = new CreateClientManagerRequest();
            ReflectionTestUtils.setField(mReq, "clientManagerStatusId", 1L);
            ReflectionTestUtils.setField(mReq, "name", "김매니저");
            ReflectionTestUtils.setField(mReq, "department", "영업부");
            ReflectionTestUtils.setField(mReq, "position", "대리");
            ReflectionTestUtils.setField(mReq, "phone", "010-1234-5678");
            ReflectionTestUtils.setField(mReq, "telephone", "02-5555-1234");
            ReflectionTestUtils.setField(mReq, "email", "kyok0510@gmail.com");
            ReflectionTestUtils.setField(mReq, "notes", "비고");

            CreateClientCompanyRequest cReq = new CreateClientCompanyRequest();
            ReflectionTestUtils.setField(cReq, "clientCompanyName", "ABC 주식회사");
            ReflectionTestUtils.setField(cReq, "clientCompanyStatusId", 2L);
            ReflectionTestUtils.setField(cReq, "businessId", 1234567890L);
            ReflectionTestUtils.setField(cReq, "sales", 100_000_000L);
            ReflectionTestUtils.setField(cReq, "numberOfEmployees", 10);
            ReflectionTestUtils.setField(cReq, "telephone", "02-0000-1111");
            ReflectionTestUtils.setField(cReq, "fax", "02-0000-2222");
            ReflectionTestUtils.setField(cReq, "address", "서울특별시 강남구");
            ReflectionTestUtils.setField(cReq, "detailAddress", "101동 202호");
            ReflectionTestUtils.setField(cReq, "notes", "테스트 메모");
            ReflectionTestUtils.setField(cReq, "clientManagers", List.of(mReq));

            // 상태 엔티티 Mock
            ClientCompanyStatus companyStatus = mock(ClientCompanyStatus.class);
            ClientManagerStatus managerStatus = mock(ClientManagerStatus.class);

            when(clientCompanyStatusRepository.findById(2L)).thenReturn(Optional.of(companyStatus));
            when(clientManagerStatusRepository.findById(1L)).thenReturn(Optional.of(managerStatus));

            ArgumentCaptor<ClientCompany> captor = ArgumentCaptor.forClass(ClientCompany.class);

            // ===== when =====
            sut.createClientCompany(cReq);

            // ===== then =====
            verify(clientCompanyRepository, times(1)).save(captor.capture());
            ClientCompany saved = captor.getValue();

            assertThat(saved.getClientCompanyName()).isEqualTo("ABC 주식회사");
            assertThat(saved.getClientManagers()).hasSize(1);
            assertThat(saved.getClientManagers().get(0).getName()).isEqualTo("김매니저");
        }

        @Test
        @DisplayName("잘못된 고객사 상태 ID면 BusinessException")
        void invalidCompanyStatus() {
            // given
            CreateClientCompanyRequest req = new CreateClientCompanyRequest();
            ReflectionTestUtils.setField(req, "clientCompanyName", "ABC");
            ReflectionTestUtils.setField(req, "clientCompanyStatusId", 999L);
            ReflectionTestUtils.setField(req, "clientManagers", Collections.emptyList());

            when(clientCompanyStatusRepository.findById(999L)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.createClientCompany(req))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(ClientErrorCode.INVALID_STATUS.getMessage());
        }
    }
}