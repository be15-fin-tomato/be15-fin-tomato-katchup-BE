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
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.service.FileService;
import be15fintomatokatchupbe.relation.service.HashInfCampService;
import be15fintomatokatchupbe.relation.service.PipeInfClientManagerService;
import be15fintomatokatchupbe.relation.service.PipeUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import com.google.firebase.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListupCommandService {
    private final PipeUserService pipeUserService;
    private final PipeInfClientManagerService pipeInfClientManagerService;
    private final HashInfCampService hashInfCampService;
    private final FileService fileService;

    private final ClientHelperService clientHelperService;
    private final CampaignHelperService campaignHelperService;
    private final UserHelperService userHelperService;

    private final CampaignRepository campaignRepository;
    private final PipelineRepository pipelineRepository;
    private final PipelineStepRepository pipelineStepRepository;
    private final PipelineStatusRepository pipelineStatusRepository;
    private final IdeaRepository ideaRepository;

    @Transactional
    public void createListup(Long userId, CreateListupRequest request) {
        PipelineStep listupStep = pipelineStepRepository.findById(PipelineStepConstants.LIST_UP)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STEP_NOT_FOUND));
        Campaign campaign = campaignHelperService.findValidCampaign(request.campaignId);
        User writer = userHelperService.findValidUser(userId);

        Pipeline pipeline = Pipeline.builder()
                .pipelineStep(listupStep)
                .name(request.getName())
                .writer(writer)
                .campaign(campaign)
                .build();

        pipelineRepository.save(pipeline);

        pipeInfClientManagerService.saveInfluencer(request.getInfluencerId(), pipeline);
    }

    @Transactional
    public void updateListup(Long userId, UpdateListupRequest request) {
        Campaign campaign = campaignHelperService.findValidCampaign(request.campaignId);

        Pipeline foundPipeline = campaignHelperService.findValidPipeline(request.getPipelineId());

        if(!Objects.equals(foundPipeline.getPipelineStep().getPipelineStepId(), PipelineStepConstants.LIST_UP)){
            throw new BusinessException(CampaignErrorCode.INVALID_ACCESS);
        }

        foundPipeline.updateListup(
                campaign,
                request.getName()
        );

        pipeInfClientManagerService.deleteByPipeline(foundPipeline);

        pipeInfClientManagerService.saveInfluencer(request.getInfluencerId(), foundPipeline);
    }

    @Transactional
    public void deleteListup(Long pipelineId) {
        Pipeline foundPipeline = campaignHelperService.findValidPipeline(pipelineId);

        if(!Objects.equals(foundPipeline.getPipelineStep().getPipelineStepId(), PipelineStepConstants.LIST_UP)){
            throw new BusinessException(CampaignErrorCode.INVALID_ACCESS);
        }
        foundPipeline.softDelete();

        campaignHelperService.deleteRelationTable(foundPipeline);
    }

    public void deleteAllListup(Long pipelineId) {
        Pipeline foundPipeline = campaignHelperService.findValidAllPipeline(pipelineId);

        if(!Objects.equals(foundPipeline.getPipelineStep().getPipelineStepId(), PipelineStepConstants.LIST_UP)){
            throw new BusinessException(CampaignErrorCode.INVALID_ACCESS);
        }
        foundPipeline.softDelete();

        campaignHelperService.deleteRelationTable(foundPipeline);
    }
}
