package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateChanceRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateQuotationRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.InfluencerProposalRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.relation.service.HashInfCampService;
import be15fintomatokatchupbe.relation.service.PipeInfClientManagerService;
import be15fintomatokatchupbe.relation.service.PipeUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignCommandServiceTest {
    @InjectMocks
    private CampaignCommandService campaignCommandService;

    @Mock
    private PipeUserService pipeUserService;
    @Mock private PipeInfClientManagerService pipeInfClientManagerService;
    @Mock private HashInfCampService hashInfCampService;
    @Mock private ClientHelperService clientHelperService;
    @Mock private CampaignHelperService campaignHelperService;
    @Mock private UserHelperService userHelperService;
    @Mock private CampaignRepository campaignRepository;
    @Mock private CampaignStatusRepository campaignStatusRepository;
    @Mock private PipelineRepository pipelineRepository;
    @Mock private PipelineStepRepository pipelineStepRepository;
    @Mock private PipelineStatusRepository pipelineStatusRepository;

    private final Long userId = 1L;

    @Test
    void createChance_shouldCreateCampaignAndPipelineSuccessfully() {
        // given
        CreateChanceRequest request = CreateChanceRequest.builder()
                .clientCompanyId(1L)
                .clientManagerId(1L)
                .campaignStatusId(3L)
                .campaignName("Test Campaign")
                .productName("Test Product")
                .productPrice(10000L)
                .awarenessPath("YouTube")
                .startedAt(LocalDateTime.now())
                .endedAt(LocalDateTime.now().plusDays(3))
                .expectedRevenue(100000L)
                .expectedProfitMargin(BigDecimal.valueOf(20))
                .userList(List.of(100L, 200L))
                .categoryList(List.of(1L, 2L))
                .build();

        when(clientHelperService.findValidClientCompany(1L)).thenReturn(mock(ClientCompany.class));
        when(clientHelperService.findValidClientManager(1L)).thenReturn(mock(ClientManager.class));
        when(campaignStatusRepository.findById(3L)).thenReturn(Optional.of(mock(CampaignStatus.class)));
        when(pipelineStepRepository.findById(PipelineStepConstants.CHANCE)).thenReturn(Optional.of(mock(PipelineStep.class)));
        when(userHelperService.findValidUser(userId)).thenReturn(mock(User.class));

        // when
        campaignCommandService.createChance(userId, request);

        // then
        verify(campaignRepository).save(any(Campaign.class));
        verify(pipelineRepository).save(any(Pipeline.class));
        verify(pipeInfClientManagerService).saveClientManager(any(), any());
        verify(pipeUserService).saveUserList(eq(List.of(100L, 200L)), any());
        verify(hashInfCampService).updateCampaignTags(any(), eq(List.of(1L, 2L)));
    }

    @Test
    void createChance_shouldThrow_whenCampaignStatusNotFound() {
        // given
        CreateChanceRequest request = CreateChanceRequest.builder()
                .clientCompanyId(10L)
                .clientManagerId(20L)
                .campaignStatusId(999L)
                .campaignName("Test Campaign")
                .build();

        when(clientHelperService.findValidClientCompany(10L)).thenReturn(mock(ClientCompany.class));
        when(clientHelperService.findValidClientManager(20L)).thenReturn(mock(ClientManager.class));
        when(campaignStatusRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> campaignCommandService.createChance(userId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(CampaignErrorCode.CAMPAIGN_STATUS_NOT_FOUND.getMessage());
    }

    @Test
    void createChance_shouldThrow_whenPipelineStepNotFound() {
        // given
        CreateChanceRequest request = CreateChanceRequest.builder()
                .clientCompanyId(10L)
                .clientManagerId(20L)
                .campaignStatusId(30L)
                .campaignName("Test Campaign")
                .build();

        when(clientHelperService.findValidClientCompany(10L)).thenReturn(mock(ClientCompany.class));
        when(clientHelperService.findValidClientManager(20L)).thenReturn(mock(ClientManager.class));
        when(campaignStatusRepository.findById(30L)).thenReturn(Optional.of(mock(CampaignStatus.class)));
        when(pipelineStepRepository.findById(PipelineStepConstants.CHANCE)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> campaignCommandService.createChance(userId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(CampaignErrorCode.PIPELINE_STEP_NOT_FOUND.getMessage());
    }

    @Test
    void createQuotation_shouldCreateSuccessfully() {
        // given
        CreateQuotationRequest request = CreateQuotationRequest.builder()
                .campaignId(1L)
                .pipelineStatusId(2L)
                .clientManagerId(3L)
                .name("Quotation Test")
                .requestAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now().plusDays(1))
                .endedAt(LocalDateTime.now().plusDays(2))
                .presentedAt(LocalDateTime.now().plusDays(1))
                .content("견적 내용")
                .notes("비고")
                .expectedRevenue(100000L)
                .expectedProfit(25000L)
                .availableQuantity(50L)
                .userId(List.of(10L, 20L))
                .influencerId(List.of(101L, 102L))
                .build();

        ClientManager mockManager = mock(ClientManager.class);
        Campaign mockCampaign = mock(Campaign.class);
        PipelineStep mockStep = mock(PipelineStep.class);
        PipelineStatus mockStatus = mock(PipelineStatus.class);
        User mockWriter = mock(User.class);

        when(clientHelperService.findValidClientManager(3L)).thenReturn(mockManager);
        when(campaignHelperService.findValidCampaign(1L)).thenReturn(mockCampaign);
        when(pipelineStepRepository.findById(PipelineStepConstants.QUOTATION)).thenReturn(Optional.of(mockStep));
        when(pipelineStatusRepository.findById(2L)).thenReturn(Optional.of(mockStatus));
        when(userHelperService.findValidUser(userId)).thenReturn(mockWriter);

        // when
        campaignCommandService.createQuotation(userId, request);

        // then
        verify(mockCampaign).updateAvailableQuantity(50L);
        verify(pipelineRepository).save(any(Pipeline.class));
        verify(pipeInfClientManagerService).saveClientManager(eq(mockManager), any(Pipeline.class));
        verify(pipeInfClientManagerService).saveInfluencer(eq(List.of(101L, 102L)), any(Pipeline.class)); // ✅ id만 검증
        verify(pipeUserService).saveUserList(eq(List.of(10L, 20L)), any(Pipeline.class));
    }


}