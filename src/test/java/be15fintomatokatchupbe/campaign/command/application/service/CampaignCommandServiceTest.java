package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateChanceRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.UpdateChanceRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.campaign.query.mapper.CampaignCommandMapper;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.domain.entity.Contract;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractRepository;
import be15fintomatokatchupbe.email.command.domain.aggregate.Satisfaction;
import be15fintomatokatchupbe.email.command.domain.repository.SatisfactionRepository;
import be15fintomatokatchupbe.relation.domain.HashtagInfluencerCampaign;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import be15fintomatokatchupbe.relation.repository.HashInfCampRepository;
import be15fintomatokatchupbe.relation.repository.PipeInfClientManagerRepository;
import be15fintomatokatchupbe.relation.service.HashInfCampService;
import be15fintomatokatchupbe.relation.service.PipeInfClientManagerService;
import be15fintomatokatchupbe.relation.service.PipeUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CampaignCommandServiceTest {

    private CampaignCommandService service;

    private PipeUserService pipeUserService;
    private PipeInfClientManagerService pipeInfClientManagerService;
    private HashInfCampService hashInfCampService;
    private ClientHelperService clientHelperService;
    private CampaignHelperService campaignHelperService;
    private UserHelperService userHelperService;

    private CampaignRepository campaignRepository;
    private CampaignStatusRepository campaignStatusRepository;
    private PipelineStepRepository pipelineStepRepository;
    private PipelineRepository pipelineRepository;
    private SatisfactionRepository satisfactionRepository;
    private ContractRepository contractRepository;
    private PipeInfClientManagerRepository pipeInfClientManagerRepository;
    private HashInfCampRepository hashInfCampRepository;
    private IdeaRepository ideaRepository;
    private CampaignCommandMapper campaignCommandMapper;

    @BeforeEach
    void setup() {
        pipeUserService = mock(PipeUserService.class);
        pipeInfClientManagerService = mock(PipeInfClientManagerService.class);
        hashInfCampService = mock(HashInfCampService.class);
        clientHelperService = mock(ClientHelperService.class);
        campaignHelperService = mock(CampaignHelperService.class);
        userHelperService = mock(UserHelperService.class);

        campaignRepository = mock(CampaignRepository.class);
        campaignStatusRepository = mock(CampaignStatusRepository.class);
        pipelineStepRepository = mock(PipelineStepRepository.class);
        pipelineRepository = mock(PipelineRepository.class);
        satisfactionRepository = mock(SatisfactionRepository.class);
        contractRepository = mock(ContractRepository.class);
        pipeInfClientManagerRepository = mock(PipeInfClientManagerRepository.class);
        hashInfCampRepository = mock(HashInfCampRepository.class);
        ideaRepository = mock(IdeaRepository.class);

        campaignCommandMapper = mock(CampaignCommandMapper.class);

        service = new CampaignCommandService(
                pipeUserService,
                pipeInfClientManagerService,
                hashInfCampService,
                clientHelperService,
                campaignHelperService,
                userHelperService,
                campaignRepository,
                campaignStatusRepository,
                pipelineRepository,
                pipelineStepRepository,
                satisfactionRepository,
                contractRepository,
                pipeInfClientManagerRepository,
                hashInfCampRepository,
                ideaRepository,
                campaignCommandMapper,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void createChance_shouldCreateCampaignAndPipelineSuccessfully() {
        Long userId = 1L;
        CreateChanceRequest request = CreateChanceRequest.builder()
                .campaignName("캠페인")
                .clientCompanyId(1L)
                .clientManagerId(2L)
                .campaignStatusId(3L)
                .productName("상품")
                .productPrice(10000L)
                .expectedRevenue(50000L)
                .expectedProfitMargin(BigDecimal.valueOf(0.25))
                .userList(List.of(1L, 2L))
                .categoryList(List.of(101L, 102L))
                .build();

        when(clientHelperService.findValidClientCompany(1L)).thenReturn(mock(ClientCompany.class));
        when(clientHelperService.findValidClientManager(2L)).thenReturn(mock(ClientManager.class));
        when(campaignStatusRepository.findById(3L)).thenReturn(Optional.of(mock(CampaignStatus.class)));
        when(pipelineStepRepository.findById(PipelineStepConstants.CHANCE)).thenReturn(Optional.of(mock(PipelineStep.class)));
        when(userHelperService.findValidUser(userId)).thenReturn(mock(User.class));

        service.createChance(userId, request);

        verify(campaignRepository).save(any(Campaign.class));
        verify(pipelineRepository).save(any(Pipeline.class));
        verify(pipeInfClientManagerService).saveClientManager(any(), any());
        verify(pipeUserService).saveUserList(eq(List.of(1L, 2L)), any());
        verify(hashInfCampService).updateCampaignTags(any(), eq(List.of(101L, 102L)));
    }

    @Test
    void updateChance_shouldUpdateCampaignAndPipeline() {
        Long campaignId = 10L;

        UpdateChanceRequest request = UpdateChanceRequest.builder()
                .campaignId(campaignId)
                .campaignName("업데이트 캠페인")
                .clientCompanyId(1L)
                .clientManagerId(2L)
                .campaignStatusId(3L)
                .productName("상품")
                .productPrice(12345L)
                .expectedRevenue(50000L)
                .expectedProfitMargin(BigDecimal.valueOf(0.3))
                .notes("비고")
                .categoryList(List.of(1L, 2L))
                .build();

        Campaign campaign = mock(Campaign.class);
        CampaignStatus status = CampaignStatus.builder().campaignStatusId(5L).build();
        Pipeline pipeline = mock(Pipeline.class);
        PipelineInfluencerClientManager picm = mock(PipelineInfluencerClientManager.class);
        ClientManager existingManager = mock(ClientManager.class);
        HashtagInfluencerCampaign dummyTag = mock(HashtagInfluencerCampaign.class);

        when(campaign.getCampaignStatus()).thenReturn(status);
        when(campaignHelperService.findValidCampaign(campaignId)).thenReturn(campaign);
        when(campaignStatusRepository.findById(3L)).thenReturn(Optional.of(mock(CampaignStatus.class)));
        when(clientHelperService.findValidClientCompany(1L)).thenReturn(mock(ClientCompany.class));
        when(pipelineRepository.findByPipelineId(campaignId)).thenReturn(pipeline);
        when(pipeline.getPipelineId()).thenReturn(campaignId);
        when(pipeInfClientManagerRepository.findByPipelineInfluencerId(campaignId)).thenReturn(picm);
        when(picm.getClientManager()).thenReturn(existingManager);
        when(existingManager.getClientManagerId()).thenReturn(999L);
        when(clientHelperService.findValidClientManager(2L)).thenReturn(mock(ClientManager.class));
        when(hashInfCampRepository.findByCampaignId(campaignId)).thenReturn(List.of(dummyTag));

        service.updateChance(request);

        verify(campaignRepository).save(campaign);
        verify(pipelineRepository).findByPipelineId(campaignId);
        verify(pipeline).update(eq(BigDecimal.valueOf(0.3)), eq(50000L), eq("비고"));
        verify(pipeInfClientManagerRepository).deleteById(anyLong());
        verify(pipeInfClientManagerRepository).save(any(PipelineInfluencerClientManager.class));
        verify(hashInfCampRepository).deleteAll(anyList());
        verify(hashInfCampRepository, times(2)).save(any(HashtagInfluencerCampaign.class));
    }

    @Test
    void deleteCampaign_shouldSoftDeleteCampaignAndIdeas() {
        Long campaignId = 5L;

        Campaign campaign = mock(Campaign.class);
        Pipeline pipeline1 = mock(Pipeline.class);
        Pipeline pipeline2 = mock(Pipeline.class);
        Idea idea = mock(Idea.class);

        when(campaignHelperService.findValidCampaign(campaignId)).thenReturn(campaign);
        when(pipelineRepository.findAllByCampaign_CampaignId(campaignId)).thenReturn(List.of(pipeline1, pipeline2));
        when(pipeline1.getPipelineStep()).thenReturn(PipelineStep.builder().pipelineStepId(1L).build());
        when(pipeline2.getPipelineStep()).thenReturn(PipelineStep.builder().pipelineStepId(8L).build());
        when(pipeline1.getPipelineId()).thenReturn(101L);
        when(pipeline2.getPipelineId()).thenReturn(102L);
        when(ideaRepository.findAllByPipeline_PipelineId(101L)).thenReturn(List.of(idea));
        when(ideaRepository.findAllByPipeline_PipelineId(102L)).thenReturn(Collections.emptyList());

        service.deleteCampaign(campaignId);

        verify(campaign).softDelete();
        verify(pipeline1).softDelete();
        verify(pipeline2).softDelete();
        verify(idea).softDelete();
    }

    @Test
    void updateChance_shouldNotInsertSatisfactionAndContractIfStatusIsNot5() {
        UpdateChanceRequest request = UpdateChanceRequest.builder()
                .campaignId(10L)
                .campaignName("Test")
                .campaignStatusId(2L)
                .clientCompanyId(1L)
                .clientManagerId(2L)
                .categoryList(List.of(1L))
                .build();

        CampaignStatus status = CampaignStatus.builder().campaignStatusId(2L).build();
        Campaign campaign = mock(Campaign.class);
        Pipeline pipeline = mock(Pipeline.class);
        PipelineInfluencerClientManager picm = mock(PipelineInfluencerClientManager.class);
        ClientManager existingManager = mock(ClientManager.class);

        when(campaignHelperService.findValidCampaign(10L)).thenReturn(campaign);
        when(campaignStatusRepository.findById(2L)).thenReturn(Optional.of(status));
        when(clientHelperService.findValidClientCompany(1L)).thenReturn(mock(ClientCompany.class));
        when(pipelineRepository.findByPipelineId(10L)).thenReturn(pipeline);
        when(pipeInfClientManagerRepository.findByPipelineInfluencerId(any())).thenReturn(picm);
        when(picm.getClientManager()).thenReturn(existingManager);
        when(existingManager.getClientManagerId()).thenReturn(3L);
        when(clientHelperService.findValidClientManager(2L)).thenReturn(mock(ClientManager.class));
        when(hashInfCampRepository.findByCampaignId(10L)).thenReturn(Collections.emptyList());

        when(campaign.getCampaignStatus()).thenReturn(status);

        service.updateChance(request);

        verify(satisfactionRepository, never()).save(any());
        verify(contractRepository, never()).save(any());
    }

    @Test
    void updateChance_shouldThrowIfCampaignStatusNotFound() {
        UpdateChanceRequest request = UpdateChanceRequest.builder()
                .campaignId(10L)
                .campaignStatusId(999L)
                .build();

        when(campaignHelperService.findValidCampaign(10L)).thenReturn(mock(Campaign.class));
        when(campaignStatusRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.updateChance(request));
    }
}
