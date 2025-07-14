package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateListupRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.UpdateListupRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Campaign;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStep;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.service.FileService;
import be15fintomatokatchupbe.relation.service.HashInfCampService;
import be15fintomatokatchupbe.relation.service.PipeInfClientManagerService;
import be15fintomatokatchupbe.relation.service.PipeUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListupCommandServiceTest {

    private ListupCommandService service;

    private PipeUserService pipeUserService;
    private PipeInfClientManagerService pipeInfClientManagerService;
    private HashInfCampService hashInfCampService;
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
        hashInfCampService = mock(HashInfCampService.class);
        fileService = mock(FileService.class);
        clientHelperService = mock(ClientHelperService.class);
        campaignHelperService = mock(CampaignHelperService.class);
        userHelperService = mock(UserHelperService.class);
        campaignRepository = mock(CampaignRepository.class);
        pipelineRepository = mock(PipelineRepository.class);
        pipelineStepRepository = mock(PipelineStepRepository.class);
        pipelineStatusRepository = mock(PipelineStatusRepository.class);
        ideaRepository = mock(IdeaRepository.class);

        service = new ListupCommandService(
                pipeUserService, pipeInfClientManagerService, hashInfCampService, fileService,
                clientHelperService, campaignHelperService, userHelperService,
                campaignRepository, pipelineRepository, pipelineStepRepository,
                pipelineStatusRepository, ideaRepository
        );
    }

    @Test
    void createListup_success() {
        CreateListupRequest request = CreateListupRequest.builder()
                .campaignId(1L)
                .name("테스트 리스트업")
                .influencerId(List.of(999L))
                .build();

        User user = User.builder().userId(1L).build();
        Campaign campaign = Campaign.builder().campaignId(1L).build();
        PipelineStep step = PipelineStep.builder().pipelineStepId(PipelineStepConstants.LIST_UP).build();

        when(pipelineStepRepository.findById(PipelineStepConstants.LIST_UP)).thenReturn(Optional.of(step));
        when(campaignHelperService.findValidCampaign(1L)).thenReturn(campaign);
        when(userHelperService.findValidUser(1L)).thenReturn(user);

        service.createListup(1L, request);

        verify(pipelineRepository, times(1)).save(any(Pipeline.class));
        verify(pipeInfClientManagerService, times(1)).saveInfluencer(eq(List.of(999L)), any(Pipeline.class));
    }

    @Test
    void updateListup_invalidStep_shouldThrowException() {
        UpdateListupRequest request = UpdateListupRequest.builder()
                .campaignId(1L)
                .pipelineId(2L)
                .name("업데이트 리스트업")
                .influencerId(List.of(999L))
                .build();

        Campaign campaign = Campaign.builder().campaignId(1L).build();
        PipelineStep wrongStep = PipelineStep.builder().pipelineStepId(999L).build();
        Pipeline pipeline = Pipeline.builder().pipelineId(2L).pipelineStep(wrongStep).build();

        when(campaignHelperService.findValidCampaign(1L)).thenReturn(campaign);
        when(campaignHelperService.findValidPipeline(2L)).thenReturn(pipeline);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.updateListup(1L, request);
        });

        assertEquals(CampaignErrorCode.INVALID_ACCESS, ex.getErrorCode());
    }

    @Test
    void deleteListup_invalidStep_shouldThrowException() {
        PipelineStep wrongStep = PipelineStep.builder().pipelineStepId(99L).build();
        Pipeline pipeline = Pipeline.builder().pipelineId(5L).pipelineStep(wrongStep).build();

        when(campaignHelperService.findValidPipeline(5L)).thenReturn(pipeline);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.deleteListup(5L);
        });

        assertEquals(CampaignErrorCode.INVALID_ACCESS, ex.getErrorCode());
    }

    @Test
    void deleteListup_success() {
        PipelineStep listupStep = PipelineStep.builder().pipelineStepId(PipelineStepConstants.LIST_UP).build();
        Pipeline pipeline = Pipeline.builder().pipelineId(10L).pipelineStep(listupStep).build();

        when(campaignHelperService.findValidPipeline(10L)).thenReturn(pipeline);

        service.deleteListup(10L);

        assertEquals(StatusType.Y, pipeline.getIsDeleted());
        verify(campaignHelperService).deleteRelationTable(pipeline);
    }

    @Test
    void deleteAllListup_success() {
        PipelineStep listupStep = PipelineStep.builder().pipelineStepId(PipelineStepConstants.LIST_UP).build();
        Pipeline pipeline = Pipeline.builder().pipelineId(11L).pipelineStep(listupStep).build();

        when(campaignHelperService.findValidAllPipeline(11L)).thenReturn(pipeline);

        service.deleteAllListup(11L);

        assertEquals(StatusType.Y, pipeline.getIsDeleted());
        verify(campaignHelperService).deleteRelationTable(pipeline);
    }
}
