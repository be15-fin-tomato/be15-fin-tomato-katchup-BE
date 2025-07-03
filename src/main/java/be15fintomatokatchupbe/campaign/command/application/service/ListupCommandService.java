package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateListupRequest;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        pipeInfClientManagerService.saveInfluencer(request.influencerId, pipeline);
    }
}
