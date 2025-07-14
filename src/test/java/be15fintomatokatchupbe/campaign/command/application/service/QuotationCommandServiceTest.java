package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateQuotationRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.IdeaRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.CampaignStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.relation.service.PipeInfClientManagerService;
import be15fintomatokatchupbe.relation.service.PipeUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class QuotationCommandServiceTest {

    private QuotationCommandService service;

    private PipeUserService pipeUserService;
    private PipeInfClientManagerService pipeInfClientManagerService;
    private ClientHelperService clientHelperService;
    private CampaignHelperService campaignHelperService;
    private UserHelperService userHelperService;
    private PipelineRepository pipelineRepository;
    private PipelineStepRepository pipelineStepRepository;
    private PipelineStatusRepository pipelineStatusRepository;
    private IdeaRepository ideaRepository;

    @BeforeEach
    void setUp() {
        pipeUserService = mock(PipeUserService.class);
        pipeInfClientManagerService = mock(PipeInfClientManagerService.class);
        clientHelperService = mock(ClientHelperService.class);
        campaignHelperService = mock(CampaignHelperService.class);
        userHelperService = mock(UserHelperService.class);
        pipelineRepository = mock(PipelineRepository.class);
        pipelineStepRepository = mock(PipelineStepRepository.class);
        pipelineStatusRepository = mock(PipelineStatusRepository.class);
        ideaRepository = mock(IdeaRepository.class);

        service = new QuotationCommandService(
                pipeUserService, pipeInfClientManagerService,
                clientHelperService, campaignHelperService, userHelperService,
                pipelineRepository, pipelineStepRepository, pipelineStatusRepository,
                ideaRepository
        );
    }

    @Test
    void createQuotation_success() {
        // Arrange
        Long userId = 1L;
        CreateQuotationRequest request = CreateQuotationRequest.builder()
                .campaignId(10L)
                .clientManagerId(20L)
                .pipelineStatusId(PipelineStatusConstants.REQUEST)
                .name("견적서1")
                .requestAt(LocalDate.now())
                .startedAt(LocalDate.now().plusDays(1))
                .endedAt(LocalDate.now().plusDays(5))
                .presentedAt(LocalDate.now().plusDays(2))
                .content("내용")
                .notes("비고")
                .expectedRevenue(1000000L)
                .expectedProfit(500000L)
                .availableQuantity(10L)  // Long 리터럴 사용
                .influencerId(List.of(101L)) // List<Long>로 전달
                .userId(List.of(201L))
                .ideaList(List.of(
                        IdeaRequest.builder().content("아이디어1").build(),
                        IdeaRequest.builder().content("아이디어2").build()
                ))
                .build();

        Campaign campaign = Campaign.builder()
                .campaignId(10L)
                .campaignStatus(CampaignStatus.builder().campaignStatusId(CampaignStatusConstants.IN_PROGRESS).build())
                .build();

        when(pipelineRepository.findApprovePipeline(10L, PipelineStepConstants.QUOTATION, PipelineStatusConstants.APPROVED)).thenReturn(null);
        when(campaignHelperService.findValidCampaign(10L)).thenReturn(campaign);
        when(clientHelperService.findValidClientManager(20L)).thenReturn(ClientManager.builder().clientManagerId(20L).build());
        when(pipelineStepRepository.findById(PipelineStepConstants.QUOTATION)).thenReturn(Optional.of(PipelineStep.builder().pipelineStepId(PipelineStepConstants.QUOTATION).build()));
        when(pipelineStatusRepository.findById(PipelineStatusConstants.REQUEST)).thenReturn(Optional.of(PipelineStatus.builder().pipelineStatusId(PipelineStatusConstants.REQUEST).build()));
        when(userHelperService.findValidUser(userId)).thenReturn(User.builder().userId(userId).build());

        // Act & Assert
        assertDoesNotThrow(() -> service.createQuotation(userId, request));

        // Verify
        verify(pipelineRepository, times(1)).save(any(Pipeline.class));
        verify(pipeInfClientManagerService).saveClientManager(any(), any());
        verify(pipeInfClientManagerService).saveInfluencer(eq(List.of(101L)), any());
        verify(pipeUserService).saveUserList(eq(List.of(201L)), any());
        verify(ideaRepository).saveAll(any());
    }
}
