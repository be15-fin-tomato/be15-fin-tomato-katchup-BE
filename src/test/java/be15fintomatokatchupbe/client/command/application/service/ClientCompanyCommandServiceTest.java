package be15fintomatokatchupbe.client.command.application.service;

import be15fintomatokatchupbe.client.command.application.dto.request.*;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompanyStatus;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManagerStatus;
import be15fintomatokatchupbe.client.command.domain.repository.*;
import be15fintomatokatchupbe.client.command.exception.ClientErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.relation.service.ClientCompanyUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientCompanyCommandServiceTest {

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

    @InjectMocks
    private ClientCompanyCommandService sut;

    @Nested
    @DisplayName("고객사/사원 등록")
    class CreateClientCompany {

        @Test
        @DisplayName("정상 등록!")
        void success() {
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
            ReflectionTestUtils.setField(cReq, "userIds", List.of(1L, 2L));

            ClientCompanyStatus companyStatus = mock(ClientCompanyStatus.class);
            ClientManagerStatus managerStatus = mock(ClientManagerStatus.class);
            User user1 = mock(User.class);
            User user2 = mock(User.class);

            when(clientCompanyStatusRepository.findById(2L)).thenReturn(Optional.of(companyStatus));
            when(clientManagerStatusRepository.findById(1L)).thenReturn(Optional.of(managerStatus));
            when(userHelperService.findValidUserList(List.of(1L, 2L))).thenReturn(List.of(user1, user2));

            sut.createClientCompany(cReq);

            verify(clientCompanyRepository, times(1)).save(any());
            verify(clientCompanyUserService, times(1)).createRelations(any(ClientCompany.class), eq(List.of(user1, user2)));
        }

        @Test
        @DisplayName("잘못된 고객사 상태 ID면 BusinessException")
        void invalidCompanyStatus() {
            CreateClientCompanyRequest req = new CreateClientCompanyRequest();
            ReflectionTestUtils.setField(req, "clientCompanyName", "ABC");
            ReflectionTestUtils.setField(req, "clientCompanyStatusId", 999L);
            ReflectionTestUtils.setField(req, "clientManagers", Collections.emptyList());

            when(clientCompanyStatusRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.createClientCompany(req))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(ClientErrorCode.INVALID_STATUS.getMessage());
        }
    }

    @Nested
    @DisplayName("고객사 수정")
    class UpdateClientCompany {

        @Test
        @DisplayName("고객사 수정 성공 (담당자 포함)")
        void success() {
            // ===== given =====
            Long companyId = 1L;

            UpdateClientManagerRequest mReq = new UpdateClientManagerRequest();
            ReflectionTestUtils.setField(mReq, "clientManagerId", 1L);
            ReflectionTestUtils.setField(mReq, "clientManagerStatusId", 1L);
            ReflectionTestUtils.setField(mReq, "name", "김대리");
            ReflectionTestUtils.setField(mReq, "department", "전략팀");
            ReflectionTestUtils.setField(mReq, "position", "과장");
            ReflectionTestUtils.setField(mReq, "phone", "010-1234-5678");
            ReflectionTestUtils.setField(mReq, "telephone", "02-5555-6666");
            ReflectionTestUtils.setField(mReq, "email", "manager@example.com");
            ReflectionTestUtils.setField(mReq, "notes", "비고 수정");

            UpdateClientCompanyRequest req = new UpdateClientCompanyRequest();
            ReflectionTestUtils.setField(req, "clientCompanyName", "토마토 광고");
            ReflectionTestUtils.setField(req, "clientCompanyStatusId", 2L);
            ReflectionTestUtils.setField(req, "businessId", 1112223333L);
            ReflectionTestUtils.setField(req, "sales", 9990000L);
            ReflectionTestUtils.setField(req, "numberOfEmployees", 50);
            ReflectionTestUtils.setField(req, "telephone", "02-3333-4444");
            ReflectionTestUtils.setField(req, "fax", "02-7777-8888");
            ReflectionTestUtils.setField(req, "address", "서울시 마포구");
            ReflectionTestUtils.setField(req, "detailAddress", "월드컵북로 10길");
            ReflectionTestUtils.setField(req, "notes", "VIP 고객");
            ReflectionTestUtils.setField(req, "clientManagers", List.of(mReq));
            ReflectionTestUtils.setField(req, "userIds", List.of(1L, 2L));

            ClientCompanyStatus status = ClientCompanyStatus.builder().build();
            ClientManagerStatus managerStatus = ClientManagerStatus.builder().build();

            ClientManager existingManager = ClientManager.builder()
                    .clientManagerId(1L)
                    .name("이전매니저")
                    .build();

            ClientCompany company = ClientCompany.builder()
                    .clientCompanyId(companyId)
                    .clientCompanyName("이전 회사명")
                    .build();
            company.addManager(existingManager);

            User user1 = mock(User.class);
            User user2 = mock(User.class);

            when(clientCompanyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(clientCompanyStatusRepository.findById(2L)).thenReturn(Optional.of(status));
            when(clientManagerStatusRepository.findById(1L)).thenReturn(Optional.of(managerStatus));
            when(userHelperService.findValidUserList(List.of(1L, 2L))).thenReturn(List.of(user1, user2));

            // ===== when =====
            sut.updateClientCompany(companyId, req);

            // ===== then =====
            assertThat(company.getClientCompanyName()).isEqualTo("토마토 광고");
            assertThat(company.getClientManagers()).hasSize(1);
            assertThat(company.getClientManagers().get(0).getName()).isEqualTo("김대리");

            verify(clientCompanyUserService).deleteByClientCompany(company);
            verify(clientCompanyUserService).createRelations(company, List.of(user1, user2));
            verify(clientCompanyRepository, times(1)).findById(companyId);
            verify(clientCompanyRepository, times(1)).save(company);
        }

        @Test
        @DisplayName("고객사가 존재하지 않으면 예외")
        void notFoundCompany() {
            // given
            Long companyId = 1L;
            UpdateClientCompanyRequest req = new UpdateClientCompanyRequest();
            when(clientCompanyRepository.findById(companyId)).thenReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> sut.updateClientCompany(companyId, req));

            assertThat(exception.getMessage()).isEqualTo(ClientErrorCode.NOT_FOUND.getMessage());
        }
    }
}