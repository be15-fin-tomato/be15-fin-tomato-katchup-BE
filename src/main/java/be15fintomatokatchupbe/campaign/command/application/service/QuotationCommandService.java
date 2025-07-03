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
import be15fintomatokatchupbe.common.exception.BusinessException;
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

    private final ClientHelperService clientHelperService;
    private final CampaignHelperService campaignHelperService;
    private final UserHelperService userHelperService;

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

    @Transactional
    public void updateQuotation(Long userId, UpdateQuotationRequest request) {

        /* 요청이 승인일 경우 승인 된게 있는지 체크 해주기 */
        if(Objects.equals(request.getPipelineStatusId(), PipelineStatusConstants.APPROVED)){
            Pipeline existPipeline = pipelineRepository.findApprovePipeline(
                    request.getCampaignId(),
                    PipelineStepConstants.QUOTATION,
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

        /* 연관 테이블 지워주기 */
        campaignHelperService.deleteRelationTable(foundPipeline);

        /* 파이프라인 값 입력해주기 */
        foundPipeline.updateQuotation(
                pipelineStatus,
                writer,
                request.getName(),
                request.getRequestAt(),
                request.getStartedAt(),
                request.getEndedAt(),
                request.getPresentedAt(),
                campaign,
                request.getContent(),
                request.getNotes(),
                request.getExpectedRevenue(),
                request.getExpectedProfit(),
                request.getAvailableQuantity()
        );

        /* 영속상태 조회 후 필드 값 수정하기 때문에 .save() 필요 없음!*/

        /* 연관 테이블 입력 해주기 */
        pipeInfClientManagerService.saveClientManager(clientManager, foundPipeline);
        pipeInfClientManagerService.saveInfluencer(request.getInfluencerId(), foundPipeline);
        pipeUserService.saveUserList(request.getUserId(), foundPipeline);
    }

    public void deleteQuotation(Long pipelineId) {
        // 1. 삭제할 파이프라인 찾아 주기
        Pipeline foundPipeline = campaignHelperService.findValidPipeline(pipelineId);

        if(foundPipeline.getPipelineStatus().getPipelineStatusId().equals(PipelineStatusConstants.APPROVED)){
            throw new BusinessException(CampaignErrorCode.APPROVED_PIPELINE_CANNOT_BE_DELETED);
        }

        if(!Objects.equals(foundPipeline.getPipelineStep().getPipelineStepId(), PipelineStepConstants.QUOTATION)){
            throw new BusinessException(CampaignErrorCode.INVALID_ACCESS);
        }

        // 2. 파이프라인 소프트 딜리트 하기
        foundPipeline.softDelete();

        // 3. 관련 테이블 지워주기
        campaignHelperService.deleteRelationTable(foundPipeline);
    }
}
