package be15fintomatokatchupbe.campaign.query.service;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.query.dto.mapper.ProposalCardDTO;
import be15fintomatokatchupbe.campaign.query.dto.request.PipelineSearchRequest;
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
        PipelineSearchRequest request = new PipelineSearchRequest();
        request.setPage(1);
        request.setSize(10);
        request.setSort("date");
        request.setSortOrder("asc");

        List<ProposalCardDTO> mockList = List.of(
                ProposalCardDTO.builder()
                        .pipelineId(1L)
                        .name("제안1")
                        .statusName("진행중")
                        .clientCompanyName("테스트회사")
                        .clientManagerName("김매니저")
                        .userNameInfo("사용자1")
                        .requestAt("2025-06-01")
                        .presentAt("2025-06-10")
                        .build()
        );

        when(campaignQueryMapper.findPipelineList(request, 0, 10, PipelineStepConstants.PROPOSAL)).thenReturn(mockList);
        when(campaignQueryMapper.countPipeline(request, PipelineStepConstants.PROPOSAL)).thenReturn(1);

        // when
        ProposalSearchResponse response = campaignQueryService.getProposalList(userId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getResponse()).hasSize(1);
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPagination().getTotalPage()).isEqualTo(1);

        verify(campaignQueryMapper, times(1)).findPipelineList(request, 0, 10, PipelineStepConstants.PROPOSAL);
        verify(campaignQueryMapper, times(1)).countPipeline(request, PipelineStepConstants.PROPOSAL);
    }
}
