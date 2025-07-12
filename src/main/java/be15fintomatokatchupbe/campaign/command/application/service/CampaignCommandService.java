package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.*;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
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
    private final SatisfactionRepository satisfactionRepository;
    private final ContractRepository contractRepository;
    private final PipeInfClientManagerRepository pipeInfClientManagerRepository;
    private final HashInfCampRepository hashInfCampRepository;
    private final IdeaRepository ideaRepository;

    private final CampaignCommandMapper campaignCommandMapper;
    private final ListupCommandService listupCommandService;
    private final ProposalCommandService proposalCommandService;
    private final QuotationCommandService quotationCommandService;
    private final RevenueCommandService revenueCommandService;
    private final ContractCommandService contractCommandService;

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
    public void updateChance(UpdateChanceRequest request) {
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

        // 파이프라인에서 해당 캠페인 id로 pipeline_step_id가 1인 파이프라인 id 찾기
        Pipeline pipeline = pipelineRepository.findByPipelineId(request.getCampaignId());

        pipeline.update(
                request.getExpectedProfitMargin(),
                request.getExpectedRevenue(),
                request.getNotes()
        );

        // picm 테이블에서 해당 pipeline_id 를 가진 client_manager_id 찾기
        PipelineInfluencerClientManager pipelineInfluencerClientManager =
                pipeInfClientManagerRepository.findByPipelineInfluencerId(pipeline.getPipelineId());

        Long Id = pipelineInfluencerClientManager.getClientManager().getClientManagerId();

        /* 새로 들어오는 clientMangerId가 기존과 다를 때*/
        if(!request.getClientManagerId().equals(Id)) {
            // 그 값을 지움
            pipeInfClientManagerRepository.deleteById(pipelineInfluencerClientManager.getPipelineInfluencerId());

            // 사원 정보를 갖고옴
            ClientManager clientManager = clientHelperService.findValidClientManager(request.getClientManagerId());

            PipelineInfluencerClientManager newPipeLine = PipelineInfluencerClientManager.builder()
                    .clientManager(clientManager)
                    .pipeline(pipeline)
                    .build();
            pipeInfClientManagerRepository.save(newPipeLine);
        }

        /* hashtag_influencer_campaign 테이블에서 요청한 캠페인 아이디 찾기 */
        List<HashtagInfluencerCampaign> hashtagInfluencerCampaigns =
                hashInfCampRepository.findByCampaignId(request.getCampaignId());

        /* 위에서 찾은 리스트가 비어있지않으면 테이블에서 지워주기 */
        if(!hashtagInfluencerCampaigns.isEmpty()) {
            hashInfCampRepository.deleteAll(hashtagInfluencerCampaigns);
        }

        List<Long> categoryList = request.getCategoryList();

        /* 요청값에서 받은 카테고리 넣기 */
        for (Long l : categoryList) {
            HashtagInfluencerCampaign newHash = HashtagInfluencerCampaign.builder()
                    .categoryId(l)
                    .campaignId(request.getCampaignId())
                    .build();
            hashInfCampRepository.save(newHash);
        }

        /* 캠페인 ID가 5가 됐으면 */
        if(campaign.getCampaignStatus().getCampaignStatusId() == 5L){
            List<Long> influencerId = campaignCommandMapper.getInfluencerId(request.getCampaignId());
            for (Long influencer : influencerId) {
                /*만족도 테이블에 값 삽입*/
                Satisfaction satisfaction = Satisfaction.builder()
                        .campaignId(request.getCampaignId())
                        .clientManagerId(request.getClientManagerId())
                        .influencerId(influencer)
                        .build();
                satisfactionRepository.save(satisfaction);

                /* 계약서 테이블 값 삽입 */
                Contract contract = Contract.builder()
                        .campaignId(request.getCampaignId())
                        .influencerId(influencer)
                        .build();
                contractRepository.save(contract);
            }
        }
    }

    // 캠페인 상세 삭제
    @Transactional
    public void deleteCampaign(Long campaignId) {
        log.info("[Service] deleteCampaign 실행. campaignId = {}", campaignId);

        // 존재하는 캠페인인지 확인
        Campaign campaign = campaignHelperService.findValidCampaign(campaignId);
        // 캠페인 id 지우기
        campaign.softDelete();
        /* 캠페인id에 해당하는 파이프라인 지우기 */
        List<Pipeline> pipeline = pipelineRepository.findAllByCampaign_CampaignId(campaignId);
        for (Pipeline pipeline1 : pipeline) {
            log.info("[Service] 찾은 파이프라인 ID = {}", pipeline1.getPipelineId());
        }
        for (Pipeline pipeline1 : pipeline) {
            Long pipelineStepId = pipeline1.getPipelineStep().getPipelineStepId();
            log.info("[Service] 돌고있는 파이프라인 ID = {}", pipeline1.getPipelineId());
            if(pipelineStepId == 1L) {
                /* 기회인지 */
                pipeline1.softDelete();
            } else if (pipelineStepId == 2L) {
                /* 리스트업 */
                listupCommandService.deleteAllListup(pipeline1.getPipelineId());
            } else if (pipelineStepId == 3L) {
                /* 제안 */
                proposalCommandService.deleteAllProposal(pipeline1.getPipelineId());
            } else if (pipelineStepId == 4L) {
                /* 견적 */
                quotationCommandService.deleteAllQuotation(pipeline1.getPipelineId());
            } else if (pipelineStepId == 5L) {
                /* 협상: 어디에서 이걸 기록하는지 모름.. */
                pipeline1.softDelete();
            } else if (pipelineStepId == 6L) {
                /* 계약 */
                contractCommandService.deleteAllContract(pipeline1.getPipelineId());
            } else if (pipelineStepId == 7L) {
                /* 매출 */
                revenueCommandService.deleteAllRevenue(pipeline1.getPipelineId());
            } else if (pipelineStepId == 8L) {
                /* 사후 관리 */
                pipeline1.softDelete();
            }
            /* 파이프라인에서 갖고있던 의견도 다 지우기 */
            /* 혹시 파이프라인 지울때 다 지우는지 확인 */
            List<Idea> idea = ideaRepository.findAllByPipeline_PipelineId(pipeline1.getPipelineId());
            for (Idea idea1 : idea) {
                idea1.softDelete();
            }
        }


    }

}