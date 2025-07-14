package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateContractRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.UpdateContractRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStatus;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStep;
import be15fintomatokatchupbe.campaign.command.domain.repository.CampaignRepository;
import be15fintomatokatchupbe.campaign.command.domain.repository.IdeaRepository;
import be15fintomatokatchupbe.campaign.command.domain.repository.PipelineRepository;
import be15fintomatokatchupbe.campaign.command.domain.repository.PipelineStatusRepository;
import be15fintomatokatchupbe.campaign.command.domain.repository.PipelineStepRepository;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractRepository;
import be15fintomatokatchupbe.email.command.domain.repository.SatisfactionRepository;
import be15fintomatokatchupbe.file.domain.File;
import be15fintomatokatchupbe.file.service.FileService;
import be15fintomatokatchupbe.relation.service.PipeInfClientManagerService;
import be15fintomatokatchupbe.relation.service.PipeUserService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContractCommandServiceTest {

    private ContractCommandService contractCommandService;
    private CampaignRepository campaignRepository;
    private PipelineRepository pipelineRepository;
    private PipelineStepRepository pipelineStepRepository;
    private PipelineStatusRepository pipelineStatusRepository;
    private IdeaRepository ideaRepository;
    private FileService fileService;
    private PipeUserService pipeUserService;
    private PipeInfClientManagerService pipeInfClientManagerService;
    private ContractRepository contractRepository;
    private SatisfactionRepository satisfactionRepository;
    private CampaignHelperService campaignHelperService;

    @BeforeEach
    void setUp() {
        campaignRepository = mock(CampaignRepository.class);
        pipelineRepository = mock(PipelineRepository.class);
        pipelineStepRepository = mock(PipelineStepRepository.class);
        pipelineStatusRepository = mock(PipelineStatusRepository.class);
        ideaRepository = mock(IdeaRepository.class);
        fileService = mock(FileService.class);
        pipeUserService = mock(PipeUserService.class);
        pipeInfClientManagerService = mock(PipeInfClientManagerService.class);
        contractRepository = mock(ContractRepository.class);
        satisfactionRepository = mock(SatisfactionRepository.class);
        campaignHelperService = mock(CampaignHelperService.class);

        contractCommandService = new ContractCommandService(
                pipeUserService,
                pipeInfClientManagerService,
                null,
                fileService,
                null,
                campaignHelperService,
                null,
                campaignRepository,
                pipelineRepository,
                pipelineStepRepository,
                pipelineStatusRepository,
                ideaRepository
        );
    }

    @Test
    void createContract_shouldSavePipelineAndFiles() {
        Long userId = 1L;
        CreateContractRequest request = CreateContractRequest.builder()
                .campaignId(1L)
                .clientManagerId(2L)
                .pipelineStatusId(1L)
                .name("계약 파이프라인")
                .expectedRevenue(100000L)
                .expectedProfit(20000L)
                .availableQuantity(50L)
                .build();

        when(pipelineRepository.findApprovePipeline(any(), any(), any())).thenReturn(null);
        when(pipelineStatusRepository.findById(any())).thenReturn(java.util.Optional.of(mock(be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStatus.class)));
        when(pipelineStepRepository.findById(any())).thenReturn(java.util.Optional.of(mock(be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStep.class)));
        when(fileService.uploadFile(any())).thenReturn(List.of(mock(File.class)));

        contractCommandService.createContract(userId, request, List.of(mock(MultipartFile.class)));

        verify(pipelineRepository).save(any(Pipeline.class));
        verify(fileService).uploadFile(any());
        verify(fileService).saveFile(any());
    }

    @Test
    void updateContract_shouldUpdatePipelineAndRelations() {
        Long userId = 1L;
        UpdateContractRequest request = UpdateContractRequest.builder()
                .campaignId(1L)
                .clientManagerId(2L)
                .pipelineStatusId(1L)
                .name("업데이트 계약")
                .expectedRevenue(200000L)
                .expectedProfit(50000L)
                .availableQuantity(100L)
                .build();

        be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline foundPipeline = mock(be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline.class);
        when(pipelineStatusRepository.findById(any())).thenReturn(java.util.Optional.of(mock(be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStatus.class)));
        when(foundPipeline.getPipelineStep()).thenReturn(mock(be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStep.class));
        when(foundPipeline.getPipelineStep().getPipelineStepId()).thenReturn(be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants.CONTRACT);

        when(pipelineRepository.findByPipelineId(any())).thenReturn(foundPipeline);

        contractCommandService.updateContract(userId, request, List.of());

        verify(fileService).deleteByPipeline(eq(foundPipeline), any());
        verify(pipeUserService).saveUserList(any(), eq(foundPipeline));
    }

    @Test
    void deleteContract_shouldSoftDeletePipelineAndRelations() {
        Pipeline pipeline = mock(Pipeline.class);
        PipelineStatus status = mock(PipelineStatus.class);
        PipelineStep step = mock(PipelineStep.class);

        // 승인된 상태가 아님
        when(status.getPipelineStatusId()).thenReturn(1L);
        when(step.getPipelineStepId()).thenReturn(PipelineStepConstants.CONTRACT);

        when(pipeline.getPipelineStatus()).thenReturn(status);
        when(pipeline.getPipelineStep()).thenReturn(step);
        when(campaignHelperService.findValidPipeline(any())).thenReturn(pipeline);

        contractCommandService.deleteContract(1L);

        verify(pipeline).softDelete();
        verify(fileService).deleteByPipeline(pipeline);
    }

    @Test
    void createContract_shouldThrowExceptionIfApprovedPipelineExists() {
        CreateContractRequest request = CreateContractRequest.builder()
                .campaignId(1L)
                .build();

        when(pipelineRepository.findApprovePipeline(any(), any(), any())).thenReturn(mock(Pipeline.class));

        assertThrows(BusinessException.class, () ->
                contractCommandService.createContract(1L, request, List.of())
        );
    }

    @Test
    void deleteContract_shouldThrowExceptionIfApproved() {
        Pipeline pipeline = mock(Pipeline.class);
        PipelineStatus approvedStatus = mock(PipelineStatus.class);
        when(approvedStatus.getPipelineStatusId()).thenReturn(PipelineStatusConstants.APPROVED);

        PipelineStep step = mock(PipelineStep.class);
        when(step.getPipelineStepId()).thenReturn(PipelineStepConstants.CONTRACT);

        when(pipeline.getPipelineStatus()).thenReturn(approvedStatus);
        when(pipeline.getPipelineStep()).thenReturn(step);

        when(campaignHelperService.findValidPipeline(any())).thenReturn(pipeline);

        assertThrows(BusinessException.class, () ->
                contractCommandService.deleteContract(1L)
        );
    }
}
