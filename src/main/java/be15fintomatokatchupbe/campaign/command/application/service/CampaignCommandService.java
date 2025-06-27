package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.*;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.file.service.FileService;
import be15fintomatokatchupbe.relation.service.HashInfCampService;
import be15fintomatokatchupbe.relation.service.PipeInfClientManagerService;
import be15fintomatokatchupbe.relation.service.PipeUserService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    // 캠페인 상세 수정
    @Transactional
    public void updateChance(Long userId, UpdateChanceRequest request) {
        // 로그 찍기
        log.info("[Service] updateChance 실행. campaignId = {}", request.getCampaignId());

        // 캠페인 조회
        Campaign campaign = campaignHelperService.findValidCampaign(request.getCampaignId());

        // 엔티티 조회
        CampaignStatus status = campaignStatusRepository.findById(request.getCampaignStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.CAMPAIGN_STATUS_NOT_FOUND));

        ClientCompany clientCompany = clientHelperService.findValidClientCompany(request.getClientCompanyId());

        // 업데이트
        campaign.update(
                request.getCampaignName(),
                status,
                clientCompany,
                request.getProductName(),
                request.getProductPrice(),
                request.getAwarenessPath()
        );

        // 저장
        campaignRepository.save(campaign);

        // 태그 업데이트
        hashInfCampService.updateCampaignTags(campaign, request.getCategoryList());

    }
}