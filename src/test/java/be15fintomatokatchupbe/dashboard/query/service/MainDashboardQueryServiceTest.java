package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.dashboard.query.dto.response.*;
import be15fintomatokatchupbe.dashboard.query.mapper.MainDashboardQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MainDashboardQueryServiceTest {
    @Mock
    private MainDashboardQueryMapper mainDashboardQueryMapper;

    @InjectMocks
    private MainDashboardQueryService mainDashboardQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSalesActivity_success() {
        Long userId = 1L;
        SalesActivityResponse expected = SalesActivityResponse.builder()
                .clientCompanyCount(3)
                .influencerCount(5)
                .contractCount(2)
                .pipelineCount(4)
                .build();

        when(mainDashboardQueryMapper.countSalesActivities(userId)).thenReturn(expected);

        SalesActivityResponse result = mainDashboardQueryService.getSalesActivity(userId);

        assertEquals(3, result.getClientCompanyCount());
        assertEquals(5, result.getInfluencerCount());
        assertEquals(2, result.getContractCount());
        assertEquals(4, result.getPipelineCount());
    }

    @Test
    void getClientCompany_success() {
        Long userId = 1L;
        List<ClientCompanyResponse> expected = List.of(ClientCompanyResponse.builder()
                .clientCompanyName("카카오")
                .telephone("02-1234-0001")
                .createdAt(LocalDateTime.now())
                .statusName("기존")
                .build());

        when(mainDashboardQueryMapper.findClientCompanyByUserId(userId)).thenReturn(expected);

        List<ClientCompanyResponse> result = mainDashboardQueryService.getClientCompany(userId);

        assertEquals("카카오", result.get(0).getClientCompanyName());
    }

    @Test
    void getTodaySchedule_success() {
        Long userId = 1L;
        List<TodayScheduleResponse> expected = List.of(TodayScheduleResponse.builder()
                .content("회의")
                .scheduleDate(LocalDate.of(2025, 6, 22))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .hexCode("#FF8A8A")
                .build());

        when(mainDashboardQueryMapper.findTodaySchedule(userId)).thenReturn(expected);

        List<TodayScheduleResponse> result = mainDashboardQueryService.getTodaySchedule(userId);

        assertEquals("#FF8A8A", result.get(0).getHexCode());
    }

    @Test
    void getListupByUserId_success() {
        Long userId = 1L;
        List<ListupResponse> expected = List.of(ListupResponse.builder()
                .companyName("네이버")
                .contractName("계약 A")
                .listupTitle("리스트업 A")
                .clientManagerName("홍길동")
                .clientManagerPosition("대리")
                .build());

        when(mainDashboardQueryMapper.findListupByUserId(userId)).thenReturn(expected);

        List<ListupResponse> result = mainDashboardQueryService.getListupByUserId(userId);

        assertEquals("리스트업 A", result.get(0).getListupTitle());
    }

    @Test
    void getProposalByUserId_success() {
        Long userId = 1L;
        List<ProposalResponse> expected = List.of(ProposalResponse.builder()
                .companyName("무신사")
                .contractName("무신사 계약")
                .proposalTitle("제안서1")
                .clientManagerName("이수빈")
                .clientManagerPosition("과장")
                .statusName("승인요청")
                .createdAt(LocalDateTime.now())
                .build());

        when(mainDashboardQueryMapper.findProposalByUserId(userId)).thenReturn(expected);

        List<ProposalResponse> result = mainDashboardQueryService.getProposalByUserId(userId);

        assertEquals("제안서1", result.get(0).getProposalTitle());
    }

    @Test
    void getContractByUserId_success() {
        Long userId = 1L;
        List<ContractResponse> expected = List.of(ContractResponse.builder()
                .companyName("삼성전자")
                .contractName("Z 시리즈 계약")
                .statusName("승인완료")
                .createdAt(LocalDateTime.now())
                .build());

        when(mainDashboardQueryMapper.findContractByUserId(userId)).thenReturn(expected);

        List<ContractResponse> result = mainDashboardQueryService.getContractByUserId(userId);

        assertEquals("Z 시리즈 계약", result.get(0).getContractName());
    }

    @Test
    void getQuotationByUserId_success() {
        Long userId = 1L;
        List<QuotationResponse> expected = List.of(QuotationResponse.builder()
                .companyName("배달의민족")
                .quotationName("배민 견적")
                .expectedProfitMargin("25%")
                .createdAt(LocalDateTime.now())
                .build());

        when(mainDashboardQueryMapper.findQuotationByUserId(userId)).thenReturn(expected);

        List<QuotationResponse> result = mainDashboardQueryService.getQuotationByUserId(userId);

        assertEquals("배달의민족", result.get(0).getCompanyName());
        assertEquals("25%", result.get(0).getExpectedProfitMargin());
    }
}
