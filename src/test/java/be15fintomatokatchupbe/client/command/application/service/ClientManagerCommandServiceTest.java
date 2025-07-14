package be15fintomatokatchupbe.client.command.application.service;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerRepository;
import be15fintomatokatchupbe.client.command.exception.ClientErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientManagerCommandServiceTest {

    @InjectMocks
    private ClientManagerCommandService service;

    @Mock
    private ClientManagerRepository clientManagerRepository;

    private ClientManager clientManager;

    @BeforeEach
    void setUp() {
        clientManager = ClientManager.builder()
                .clientManagerId(1L)
                .isDeleted(StatusType.N)
                .build();
    }

    @Test
    void deleteClientManager_success() {
        // given
        when(clientManagerRepository.findByClientManagerIdAndIsDeleted(1L, StatusType.N))
                .thenReturn(Optional.of(clientManager));

        // when
        service.deleteClientManager(1L);

        // then
        assertThat(clientManager.getIsDeleted()).isEqualTo(StatusType.Y);
        assertThat(clientManager.getDeletedAt()).isNotNull();
    }

    @Test
    void deleteClientManager_notFound_throwsException() {
        // given
        when(clientManagerRepository.findByClientManagerIdAndIsDeleted(99L, StatusType.N))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.deleteClientManager(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ClientErrorCode.CLIENT_MANAGER_NOT_FOUND.getMessage());
    }
}
