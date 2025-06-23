package be15fintomatokatchupbe.campaign.query.service;

import be15fintomatokatchupbe.campaign.query.dto.request.ProposalSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalCardResponse;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalSearchResponse;
import be15fintomatokatchupbe.campaign.query.mapper.CampaignQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CampaignQueryServiceTest {

    @InjectMocks
    private CampaignQueryService campaignQueryService;

    @Mock
    private CampaignQueryMapper campaignQueryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProposalList_success() {
        // given
        Long userId = 1L;
        ProposalSearchRequest request = new ProposalSearchRequest();
        request.setPage(1);
        request.setSize(10);
        request.setSort("date");
        request.setSortOrder("asc");

        List<ProposalCardResponse> mockList = List.of(
                ProposalCardResponse.builder()
                        .pipelineId(1L)
                        .name("제안1")
                        .statusId("진행중")
                        .clientCompanyName("테스트회사")
                        .clientManagerName("김매니저")
                        .userName("사용자1")
                        .requestAt("2025-06-01")
                        .presentAt("2025-06-10")
                        .build()
        );

        when(campaignQueryMapper.findProposals(request, 0, 10)).thenReturn(mockList);
        when(campaignQueryMapper.countProposals(request)).thenReturn(1);

        // when
        ProposalSearchResponse response = campaignQueryService.getProposalList(userId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getResponse()).hasSize(1);
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPagination().getTotalPage()).isEqualTo(1);

        verify(campaignQueryMapper, times(1)).findProposals(request, 0, 10);
        verify(campaignQueryMapper, times(1)).countProposals(request);
    }
}
