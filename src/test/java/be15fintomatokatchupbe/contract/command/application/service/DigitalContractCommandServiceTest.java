package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.application.dto.request.DigitalContractEditRequest;
import be15fintomatokatchupbe.contract.command.application.dto.response.DigitalContractEditResponse;
import be15fintomatokatchupbe.contract.command.application.dto.response.DigitalContractDeleteResponse;
import be15fintomatokatchupbe.contract.command.domain.repository.DigitalContractRepository;
import be15fintomatokatchupbe.contract.exception.DigitalContractErrorCode;
import be15fintomatokatchupbe.contract.query.application.service.domain.aggregate.DigitalContract;
import be15fintomatokatchupbe.common.domain.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DigitalContractCommandServiceTest {

    private DigitalContractRepository digitalContractRepository;
    private DigitalContractCommandService sut;

    @BeforeEach
    void setUp() {
        digitalContractRepository = mock(DigitalContractRepository.class);
        sut = new DigitalContractCommandService(digitalContractRepository);
    }

    @Nested
    @DisplayName("editDigitalContract() - 계약서 수정")
    class EditDigitalContract {

        @Test
        @DisplayName("성공적으로 수정된다")
        void success() {
            Long id = 1L;
            DigitalContractEditRequest request = new DigitalContractEditRequest(id, "새 템플릿", "새 내용");

            DigitalContract contract = DigitalContract.builder()
                    .digitalContractId(id)
                    .template("이전 템플릿")
                    .content("이전 내용")
                    .isUsed(StatusType.N)
                    .build();

            when(digitalContractRepository.findById(id)).thenReturn(Optional.of(contract));

            DigitalContractEditResponse result = sut.editDigitalContract(request);

            assertThat(result.getMessage()).isEqualTo("전자 계약서가 성공적으로 수정되었습니다.");
            assertThat(contract.getTemplate()).isEqualTo("새 템플릿");
            assertThat(contract.getContent()).isEqualTo("새 내용");

            verify(digitalContractRepository).findById(id);
        }

        @Test
        @DisplayName("계약서가 없으면 예외 발생")
        void notFound() {
            Long id = 1L;
            when(digitalContractRepository.findById(id)).thenReturn(Optional.empty());

            DigitalContractEditRequest request = new DigitalContractEditRequest(id, "A", "B");

            assertThatThrownBy(() -> sut.editDigitalContract(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(DigitalContractErrorCode.NOT_FOUND.getMessage());

            verify(digitalContractRepository).findById(id);
        }

        @Test
        @DisplayName("내용이 비어 있으면 예외 발생")
        void emptyContent() {
            Long id = 1L;
            DigitalContractEditRequest request = new DigitalContractEditRequest(id, "A", "");

            DigitalContract contract = DigitalContract.builder()
                    .digitalContractId(id)
                    .template("T")
                    .content("C")
                    .isUsed(StatusType.N)
                    .build();

            when(digitalContractRepository.findById(id)).thenReturn(Optional.of(contract));

            assertThatThrownBy(() -> sut.editDigitalContract(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(DigitalContractErrorCode.EMPTY_CONTENT.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteDigitalContract() - 계약서 삭제")
    class DeleteDigitalContract {

        @Test
        @DisplayName("성공적으로 삭제된다")
        void success() {
            Long id = 1L;
            DigitalContract contract = DigitalContract.builder()
                    .digitalContractId(id)
                    .template("템플릿")
                    .content("내용")
                    .build();

            when(digitalContractRepository.findById(id)).thenReturn(Optional.of(contract));

            DigitalContractDeleteResponse response = sut.deleteDigitalContract(id);

            assertThat(response.getMessage()).isEqualTo("전자 계약서가 성공적으로 삭제되었습니다.");
            verify(digitalContractRepository).delete(contract);
        }

        @Test
        @DisplayName("존재하지 않으면 예외 발생")
        void notFound() {
            Long id = 999L;
            when(digitalContractRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.deleteDigitalContract(id))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(DigitalContractErrorCode.NOT_FOUND.getMessage());
        }
    }
}
