package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateQuotationRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.UpdateQuotationRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.domain.StatusType;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationCommandService {
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
    public void createQuotation(Long userId, CreateQuotationRequest request) {
        Pipeline existPipeline = pipelineRepository.findApprovePipeline(
                request.getCampaignId(),
                PipelineStepConstants.QUOTATION,
                PipelineStatusConstants.APPROVED
        );

        if(existPipeline != null){
            throw new BusinessException(CampaignErrorCode.APPROVED_QUOTATION_ALREADY_EXISTS);
        }

        ClientManager clientManager = clientHelperService.findValidClientManager(request.getClientManagerId());
        Campaign campaign = campaignHelperService.findValidCampaign(request.getCampaignId());
        PipelineStep pipelineStep = pipelineStepRepository.findById(PipelineStepConstants.QUOTATION)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STEP_NOT_FOUND));
        PipelineStatus pipelineStatus = pipelineStatusRepository.findById(request.getPipelineStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND));
        User writer = userHelperService.findValidUser(userId);

        Pipeline pipeline = Pipeline.builder()
                .writer(writer)
                .pipelineStep(pipelineStep)
                .pipelineStatus(pipelineStatus)
                .name(request.getName())
                .requestAt(request.getRequestAt())
                .startedAt(request.getStartedAt())
                .endedAt(request.getEndedAt())
                .presentedAt(request.getPresentedAt())
                .campaign(campaign)
                .content(request.getContent())
                .notes(request.getNotes())
                .expectedRevenue(request.getExpectedRevenue())
                .expectedProfit(request.getExpectedProfit())
                .availableQuantity(request.getAvailableQuantity())
                .build();
        pipelineRepository.save(pipeline);

        List<Idea> ideaList = Optional.ofNullable(request.getIdeaList())
                .orElse(List.of()) // null인 경우 빈 리스트로 처리
                .stream()
                .map(idea -> Idea.builder()
                        .content(idea.getContent())
                        .user(writer)
                        .pipeline(pipeline)
                        .build())
                .toList();

        if (!ideaList.isEmpty()) {
            ideaRepository.saveAll(ideaList);
        }

        pipeInfClientManagerService.saveClientManager(clientManager, pipeline);
        pipeInfClientManagerService.saveInfluencer(request.getInfluencerId(), pipeline);
        pipeUserService.saveUserList(request.getUserId(), pipeline);
    }

    public void updateQuotation(Long userId, UpdateQuotationRequest request) {

        /* 요청이 승인일 경우 승인 된게 있는지 체크 해주기 */
        if(Objects.equals(request.getPipelineStatusId(), PipelineStatusConstants.APPROVED)){
            Pipeline existPipeline = pipelineRepository.findApprovePipeline(
                    request.getCampaignId(),
                    PipelineStepConstants.REVENUE,
                    PipelineStatusConstants.APPROVED
            );

            if(existPipeline != null){
                throw new BusinessException(CampaignErrorCode.APPROVED_REVENUE_ALREADY_EXISTS);
            }
        }

        ClientManager clientManager = clientHelperService.findValidClientManager(request.getClientManagerId());
        Campaign campaign = campaignHelperService.findValidCampaign(request.getCampaignId());
        PipelineStatus pipelineStatus = pipelineStatusRepository.findById(request.getPipelineStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND));
        User writer = userHelperService.findValidUser(userId);

        /* 수정할 파이프라인 찾아주기 */
        Pipeline foundPipeline = campaignHelperService.findValidPipeline(request.getPipelineId());





    }
}
