package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateProposalRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.InfluencerProposalRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.CampaignStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.service.FileService;
import be15fintomatokatchupbe.relation.service.HashInfCampService;
import be15fintomatokatchupbe.relation.service.PipeInfClientManagerService;
import be15fintomatokatchupbe.relation.service.PipeUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProposalCommandServiceTest {

    private ProposalCommandService service;

    private PipeUserService pipeUserService;
    private PipeInfClientManagerService pipeInfClientManagerService;
    private HashInfCampService hashInfCampService;

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
        hashInfCampService = mock(HashInfCampService.class);
        clientHelperService = mock(ClientHelperService.class);
        campaignHelperService = mock(CampaignHelperService.class);
        userHelperService = mock(UserHelperService.class);
        campaignRepository = mock(CampaignRepository.class);
        pipelineRepository = mock(PipelineRepository.class);
        pipelineStepRepository = mock(PipelineStepRepository.class);
        pipelineStatusRepository = mock(PipelineStatusRepository.class);
        ideaRepository = mock(IdeaRepository.class);

        service = new ProposalCommandService(
                pipeUserService, pipeInfClientManagerService, hashInfCampService,
                clientHelperService, campaignHelperService, userHelperService,
                campaignRepository, pipelineRepository, pipelineStepRepository,
                pipelineStatusRepository, ideaRepository
        );
    }

    @Test
    void createProposal_success() {
        CreateProposalRequest request = CreateProposalRequest.builder()
                .campaignId(1L)
                .clientManagerId(2L)
                .pipelineStatusId(PipelineStatusConstants.REQUEST)
                .name("제안서")
                .requestAt(LocalDate.now())
                .startedAt(LocalDate.now())
                .endedAt(LocalDate.now().plusDays(1))
                .presentedAt(LocalDate.now())
                .influencerList(List.of(
                        InfluencerProposalRequest.builder()
                                .influencerId(101L)
                                .strength("소통능력")
                                .notes("비고")
                                .build(),
                        InfluencerProposalRequest.builder()
                                .influencerId(102L)
                                .strength("콘텐츠 다양성")
                                .notes("참고사항")
                                .build()
                ))
                .userId(List.of(201L))
                .build();

        when(pipelineRepository.findApprovePipeline(1L, PipelineStepConstants.PROPOSAL, PipelineStatusConstants.APPROVED)).thenReturn(null);
        when(clientHelperService.findValidClientManager(2L)).thenReturn(ClientManager.builder().clientManagerId(2L).build());
        when(campaignHelperService.findValidCampaign(1L)).thenReturn(Campaign.builder().campaignId(1L).campaignStatus(CampaignStatus.builder().campaignStatusId(CampaignStatusConstants.IN_PROGRESS).build()).build());
        when(pipelineStepRepository.findById(PipelineStepConstants.PROPOSAL)).thenReturn(Optional.of(PipelineStep.builder().pipelineStepId(PipelineStepConstants.PROPOSAL).build()));
        when(pipelineStatusRepository.findById(PipelineStatusConstants.REQUEST)).thenReturn(Optional.of(PipelineStatus.builder().pipelineStatusId(PipelineStatusConstants.REQUEST).build()));
        when(userHelperService.findValidUser(1L)).thenReturn(User.builder().userId(1L).build());

        assertDoesNotThrow(() -> service.createProposal(1L, request));

        verify(pipelineRepository, times(1)).save(any(Pipeline.class));
        verify(pipeInfClientManagerService).saveClientManager(any(), any());
        verify(pipeInfClientManagerService).saveInfluencerInfo(anyList(), any());
        verify(pipeUserService).saveUserList(anyList(), any());
    }

    @Test
    void deleteProposal_approved_shouldThrow() {
        Pipeline pipeline = Pipeline.builder()
                .pipelineId(99L)
                .pipelineStatus(PipelineStatus.builder().pipelineStatusId(PipelineStatusConstants.APPROVED).build())
                .pipelineStep(PipelineStep.builder().pipelineStepId(PipelineStepConstants.PROPOSAL).build())
                .build();

        when(campaignHelperService.findValidPipeline(99L)).thenReturn(pipeline);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.deleteProposal(99L));
        assertEquals(CampaignErrorCode.APPROVED_PIPELINE_CANNOT_BE_DELETED, ex.getErrorCode());
    }
}
