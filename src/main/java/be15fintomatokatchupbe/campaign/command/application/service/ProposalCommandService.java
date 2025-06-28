package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateProposalRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProposalCommandService {
    private final PipeUserService pipeUserService;
    private final PipeInfClientManagerService pipeInfClientManagerService;
    private final HashInfCampService hashInfCampService;

    private final ClientHelperService clientHelperService;
    private final CampaignHelperService campaignHelperService;
    private final UserHelperService userHelperService;

    private final CampaignRepository campaignRepository;
    private final PipelineRepository pipelineRepository;
    private final PipelineStepRepository pipelineStepRepository;
    private final PipelineStatusRepository pipelineStatusRepository;
    private final IdeaRepository ideaRepository;

    @Transactional
    public void createProposal(Long userId, CreateProposalRequest request) {
        /* 외부 엔티티 가져오기
         * : 광고 담당자, 캠페인, 파이프라인 단계 */

        //. 광고 담당자 가져오기
        ClientManager clientManager =
                clientHelperService.findValidClientManager(request.getClientManagerId());

        // 캠페인 가져오기
        Campaign campaign =
                campaignHelperService.findValidCampaign(request.getCampaignId());

        // 파이프라인 단계 가져오기
        PipelineStep pipelineStep =
                pipelineStepRepository.findById(PipelineStepConstants.PROPOSAL)
                        .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STEP_NOT_FOUND));

        // 파이프라인 상태 가져오기
        PipelineStatus pipelineStatus =
                pipelineStatusRepository.findById(request.getPipelineStatusId())
                        .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND));

        // 작성자 가져오기
        User writer = userHelperService.findValidUser(userId);

        /* DB에 값 입력하기*/
        // 파이프라인 생성 - 저장
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

        /* 부가 데이터 각 테이블에 저장하기
         * : 광고 담당자, 인플루언서 강점-비고, 담당자*/

        /* 광고 담당자 */
        pipeInfClientManagerService.saveClientManager(clientManager, pipeline);

        /* 인플루언서 정보 */
        pipeInfClientManagerService.saveInfluencerInfo(request.getInfluencerList(), pipeline);

        /* 담당자*/
        pipeUserService.saveUserList(request.getUserId(), pipeline);
    }
}
