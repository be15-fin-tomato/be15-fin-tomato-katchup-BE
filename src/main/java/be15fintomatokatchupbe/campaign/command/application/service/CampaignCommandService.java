package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateChanceRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateProposalRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateQuotationRequest;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@AllArgsConstructor
@Slf4j
@Service
public class CampaignCommandService {
    private final PipeUserService pipeUserService;
    private final PipeInfClientManagerService pipeInfClientManagerService;
    private final HashInfCampService hashInfCampService;

    private final ClientHelperService clientHelperService;
    private final CampaignHelperService campaignHelperService;
    private final UserHelperService userHelperService;

    private final CampaignRepository campaignRepository;
    private final CampaignStatusRepository campaignStatusRepository;
    private final PipelineRepository pipelineRepository;
    private final PipelineStepRepository pipelineStepRepository;
    private final PipelineStatusRepository pipelineStatusRepository;
    private final IdeaRepository ideaRepository;

    @Transactional
    public void createChance(Long userId, CreateChanceRequest request) {

        // 0. 외부 엔티티 가져오기
        // 고객사
        ClientCompany clientCompany =
                clientHelperService.findValidClientCompany(request.getClientCompanyId());

        // 사원
        ClientManager clientManager =
                clientHelperService.findValidClientManager(request.getClientManagerId());

        // 캠페인 상태 가져오기
        CampaignStatus campaignStatus = campaignStatusRepository.findById(request.getCampaignStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.CAMPAIGN_STATUS_NOT_FOUND));

        // 파이프 라인 단계
        PipelineStep pipelineStep = pipelineStepRepository.findById(PipelineStepConstants.CHANCE)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STEP_NOT_FOUND));

        // 작성자
        User writer = userHelperService.findValidUser(userId);

        // 1. 캠페인 만들기
        Campaign campaign = Campaign.builder()
                .clientCompany(clientCompany)
                .campaignStatus(campaignStatus)
                .campaignName(request.getCampaignName())
                .productName(request.getProductName())
                .productPrice(request.getProductPrice())
                .awarenessPath(request.getAwarenessPath())
                .build();

        // 1.1. 캠페인 저장 - 파이프라인과의 연관 관계를 위함
        campaignRepository.save(campaign);

        // 2. 파이프라인 만들기
        Pipeline pipeline = Pipeline.builder()
                .name(request.getCampaignName())
                .writer(writer)
                .pipelineStep(pipelineStep)
                .campaign(campaign)
                .startedAt(request.getStartedAt())
                .endedAt(request.getEndedAt())
                .expectedRevenue(request.getExpectedRevenue())
                .expectedProfitMargin(request.getExpectedProfitMargin())
                .build();

        pipelineRepository.save(pipeline);
        // 3. 광고 담당자 입력하기
        pipeInfClientManagerService.saveClientManager(clientManager, pipeline);

        // 4. 담당자 입력하기
        pipeUserService.saveUserList(request.getUserList(), pipeline);

        // 5. 해시태그 입력하기
        hashInfCampService.updateCampaignTags(campaign, request.getCategoryList());
    }

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

        List<Idea> ideaList = request
                .getIdeaList()
                .stream()
                .map(idea ->
                        Idea
                                .builder()
                                .content(idea.getContent())
                                .user(writer)
                                .pipeline(pipeline)
                                .build()
                ).toList();

        ideaRepository.saveAll(ideaList);

        /* 부가 데이터 각 테이블에 저장하기
         * : 광고 담당자, 인플루언서 강점-비고, 담당자*/

        /* 광고 담당자 */
        pipeInfClientManagerService.saveClientManager(clientManager, pipeline);

        /* 인플루언서 정보 */
        pipeInfClientManagerService.saveInfluencer(request.getInfluencerList(), pipeline);

        /* 담당자*/
        pipeUserService.saveUserList(request.getUserId(), pipeline);
    }

    @Transactional
    public void createQuotation(Long userId, CreateQuotationRequest request) {
        ClientManager clientManager = clientHelperService.findValidClientManager(request.getClientManagerId());
        Campaign campaign = campaignHelperService.findValidCampaign(request.getCampaignId());
        PipelineStep pipelineStep = pipelineStepRepository.findById(PipelineStepConstants.QUOTATION)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STEP_NOT_FOUND));
        PipelineStatus pipelineStatus = pipelineStatusRepository.findById(request.getPipelineStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND));
        User writer = userHelperService.findValidUser(userId);

        campaign.updateAvailableQuantity(request.getAvailableQuantity());

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
                .build();
        pipelineRepository.save(pipeline);

        pipeInfClientManagerService.saveClientManager(clientManager, pipeline);
        pipeInfClientManagerService.saveInfluencer(request.getInfluencerId(), pipeline);
        pipeUserService.saveUserList(request.getUserId(), pipeline);
    }
}
