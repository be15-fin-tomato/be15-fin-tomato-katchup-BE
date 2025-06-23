package be15fintomatokatchupbe.contract.query.application.service;

import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractDTO;
import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractDetailResponse;
import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractListResponse;
import be15fintomatokatchupbe.contract.query.mapper.DigitalContractMapper;
import be15fintomatokatchupbe.contract.query.service.DigitalContractQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DigitalContractQueryServiceTest {

    private DigitalContractMapper digitalContractMapper;
    private DigitalContractQueryService sut;

    @BeforeEach
    void setUp() {
        digitalContractMapper = mock(DigitalContractMapper.class);
        sut = new DigitalContractQueryService(digitalContractMapper);
    }

    @Nested
    @DisplayName("getDigital() - 템플릿 목록 조회")
    class GetDigital {

        @Test
        @DisplayName("전체 템플릿 목록을 반환한다")
        void success() {

            List<DigitalContractDTO> mockList = List.of(
                    new DigitalContractDTO(1L, "계약서 A"),
                    new DigitalContractDTO(2L, "계약서 B")
            );
            when(digitalContractMapper.findAllDigitalContracts()).thenReturn(mockList);

            DigitalContractListResponse result = sut.getDigital();

            assertThat(result.getDetail()).hasSize(2);
            assertThat(result.getDetail().get(0).getTemplate()).isEqualTo("계약서 A");
            verify(digitalContractMapper, times(1)).findAllDigitalContracts();
        }
    }

    @Nested
    @DisplayName("getDigitalDetail() - 템플릿 상세 조회")
    class GetDigitalDetail {

        @Test
        @DisplayName("ID로 상세 정보를 반환한다")
        void success() {
            Long id = 1L;
            DigitalContractDetailResponse mockResponse = new DigitalContractDetailResponse(
                    id, "계약서 A", LocalDateTime.now(), "내용 A", true
            );
            when(digitalContractMapper.findDigitalContractById(id)).thenReturn(mockResponse);

            DigitalContractDetailResponse result = sut.getDigitalDetail(id);

            assertThat(result.getDigitalContractId()).isEqualTo(1L);
            assertThat(result.getTemplate()).isEqualTo("계약서 A");
            verify(digitalContractMapper, times(1)).findDigitalContractById(id);
        }
    }
}
