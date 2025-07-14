package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateRevenueRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.IdeaRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.InfluencerRevenueRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.CampaignStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.file.service.FileService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RevenueCommandServiceTest {

    private RevenueCommandService service;

    private PipeUserService pipeUserService;
    private PipeInfClientManagerService pipeInfClientManagerService;
    private FileService fileService;
    private ClientHelperService clientHelperService;
    private CampaignHelperService campaignHelperService;
    private UserHelperService userHelperService;
    private CampaignRepository campaignRepository;
    private PipelineRepository pipelineRepository;
    private PipelineStepRepository pipelineStepRepository;
    private PipelineStatusRepository pipelineStatusRepository;
    private IdeaRepository ideaRepository;

    @BeforeEach
    void setUp() {
        pipeUserService = mock(PipeUserService.class);
        pipeInfClientManagerService = mock(PipeInfClientManagerService.class);
        fileService = mock(FileService.class);
        clientHelperService = mock(ClientHelperService.class);
        campaignHelperService = mock(CampaignHelperService.class);
        userHelperService = mock(UserHelperService.class);
        campaignRepository = mock(CampaignRepository.class);
        pipelineRepository = mock(PipelineRepository.class);
        pipelineStepRepository = mock(PipelineStepRepository.class);
        pipelineStatusRepository = mock(PipelineStatusRepository.class);
        ideaRepository = mock(IdeaRepository.class);

        service = new RevenueCommandService(
                pipeUserService,
                pipeInfClientManagerService,
                fileService,
                clientHelperService,
                campaignHelperService,
                userHelperService,
                campaignRepository,
                pipelineRepository,
                pipelineStepRepository,
                pipelineStatusRepository,
                ideaRepository
        );
    }

    @Test
    void createRevenue_success() {
        // given
        Long userId = 1L;

        CreateRevenueRequest request = CreateRevenueRequest.builder()
                .campaignId(100L)
                .clientManagerId(200L)
                .pipelineStatusId(PipelineStatusConstants.REQUEST)
                .productPrice(300000L)
                .name("매출 등록")
                .requestAt(LocalDate.now())
                .startedAt(LocalDate.now().plusDays(1))
                .endedAt(LocalDate.now().plusDays(10))
                .presentedAt(LocalDate.now().plusDays(2))
                .content("매출 내용")
                .notes("비고")
                .ideaList(List.of(
                        IdeaRequest.builder().content("아이디어1").build()
                ))
                .influencerList(List.of(
                        InfluencerRevenueRequest.builder().influencerId(101L).build()
                ))
                .userId(List.of(201L))
                .build();

        Campaign campaign = Campaign.builder()
                .campaignId(100L)
                .campaignStatus(CampaignStatus.builder().campaignStatusId(CampaignStatusConstants.IN_PROGRESS).build())
                .build();

        when(pipelineRepository.findApprovePipeline(100L, PipelineStepConstants.REVENUE, PipelineStatusConstants.APPROVED)).thenReturn(null);
        when(campaignHelperService.findValidCampaign(100L)).thenReturn(campaign);
        when(clientHelperService.findValidClientManager(200L)).thenReturn(ClientManager.builder().clientManagerId(200L).build());
        when(pipelineStepRepository.findById(PipelineStepConstants.REVENUE)).thenReturn(Optional.of(PipelineStep.builder().pipelineStepId(PipelineStepConstants.REVENUE).build()));
        when(pipelineStatusRepository.findById(PipelineStatusConstants.REQUEST)).thenReturn(Optional.of(PipelineStatus.builder().pipelineStatusId(PipelineStatusConstants.REQUEST).build()));
        when(userHelperService.findValidUser(userId)).thenReturn(User.builder().userId(userId).build());

        // when & then
        assertDoesNotThrow(() -> service.createRevenue(userId, request, List.of()));

        verify(pipelineRepository).save(any());
        verify(pipeInfClientManagerService).saveClientManager(any(), any());
        verify(pipeInfClientManagerService).saveInfluencerRevenue(eq(request.getInfluencerList()), any());
        verify(pipeUserService).saveUserList(eq(List.of(201L)), any());
        verify(ideaRepository).saveAll(any());
    }
}
