package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.application.dto.request.ContractObjectUpdateRequest;
import be15fintomatokatchupbe.contract.command.application.dto.request.ObjectCreateRequest;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractObject;
import be15fintomatokatchupbe.contract.command.domain.entity.Detail;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.DetailRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.ObjectRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractObjectCommandServiceTest {

    @InjectMocks
    private ContractObjectCommandService service;

    @Mock
    private ObjectRepository objectRepository;

    @Mock
    private DetailRepository detailRepository;

    @Mock
    private ContractFileRepository contractFileRepository;

    @Mock
    private DetailCommandService detailCommandService;

    private ContractObject object;

    @BeforeEach
    void setUp() {
        object = ContractObject.builder()
                .objectId(1L)
                .title("기존 제목")
                .build();
    }

    @Test
    void createObject_success() {
        // given
        ObjectCreateRequest request = ObjectCreateRequest.builder()
                .title("새 객체")
                .build();

        // when
        service.createObject(request);

        // then
        verify(objectRepository, times(1)).save(any(ContractObject.class));
    }

    @Test
    void updateObject_success() {
        // given
        ContractObjectUpdateRequest request = ContractObjectUpdateRequest.builder()
                .title("수정된 제목")
                .build();

        when(objectRepository.findById(1L)).thenReturn(Optional.of(object));

        // when
        service.updateObject(1L, request);

        // then
        assertThat(object.getTitle()).isEqualTo("수정된 제목");
        verify(objectRepository).save(object);
    }

    @Test
    void updateObject_notFound_throwsException() {
        // given
        when(objectRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.updateObject(99L, ContractObjectUpdateRequest.builder().title("수정").build()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ContractErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    void deleteObject_success() {
        // given
        Detail detail1 = Detail.builder().detailId(100L).build();
        Detail detail2 = Detail.builder().detailId(200L).build();

        when(objectRepository.findById(1L)).thenReturn(Optional.of(object));
        when(detailRepository.findAllByObjectId(1L)).thenReturn(List.of(detail1, detail2));

        // when
        service.deleteObject(1L);

        // then
        verify(detailCommandService).deleteDetail(100L);
        verify(detailCommandService).deleteDetail(200L);
        verify(objectRepository).delete(object);
    }

    @Test
    void deleteObject_notFound_throwsException() {
        // given
        when(objectRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.deleteObject(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ContractErrorCode.NOT_FOUND.getMessage());
    }
}
