package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateRevenueRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.UpdateRevenueRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.CampaignStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStatusConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.campaign.command.domain.repository.*;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.campaign.query.mapper.CampaignCommandMapper;
import be15fintomatokatchupbe.client.command.application.support.ClientHelperService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.domain.entity.Contract;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractRepository;
import be15fintomatokatchupbe.dashboard.command.application.support.DashboardHelperService;
import be15fintomatokatchupbe.email.command.domain.aggregate.Satisfaction;
import be15fintomatokatchupbe.email.command.domain.repository.SatisfactionRepository;
import be15fintomatokatchupbe.file.domain.File;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevenueCommandService {
    private final PipeUserService pipeUserService;
    private final PipeInfClientManagerService pipeInfClientManagerService;
    private final FileService fileService;

    private final ClientHelperService clientHelperService;
    private final CampaignHelperService campaignHelperService;
    private final UserHelperService userHelperService;
    private final DashboardHelperService dashboardHelperService;

    private final CampaignRepository campaignRepository;
    private final PipelineRepository pipelineRepository;
    private final PipelineStepRepository pipelineStepRepository;
    private final PipelineStatusRepository pipelineStatusRepository;
    private final IdeaRepository ideaRepository;
    private final ContractRepository contractRepository;
    private final SatisfactionRepository satisfactionRepository;

    private final CampaignCommandMapper campaignCommandMapper;

    @Transactional
    public void createRevenue(Long userId, CreateRevenueRequest request, List<MultipartFile> files) {
        /* 승인 완료가 존재 할 경우 더이상 등록하지 못하게 하기 ! */
        /* 해당 캠페인의 매출 중에 승인된 파이프라인이 존재할 경우 더이상 등록 불가능 */
        /* CampaignId, 스텝 ID,상태 ID, 삭제 여부로 존재 확인! */
        Pipeline existPipeline = pipelineRepository.findApprovePipeline(
                request.getCampaignId(),
                PipelineStepConstants.REVENUE,
                PipelineStatusConstants.APPROVED
        );

        if(existPipeline != null){
            throw new BusinessException(CampaignErrorCode.APPROVED_REVENUE_ALREADY_EXISTS);
        }
        Campaign campaign = campaignHelperService.findValidCampaign(request.getCampaignId());
        /* 이미 완료된 캠페인인 경우 수정 불가 */
        if(Objects.equals(campaign.getCampaignStatus().getCampaignStatusId(), CampaignStatusConstants.FINISHED)){
            throw new BusinessException(CampaignErrorCode.FINISHED_CAMPAIGN);
        }

        ClientManager clientManager = clientHelperService.findValidClientManager(request.getClientManagerId());
        PipelineStep pipelineStep = pipelineStepRepository.findById(PipelineStepConstants.REVENUE)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STEP_NOT_FOUND));
        PipelineStatus pipelineStatus = pipelineStatusRepository.findById(request.getPipelineStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND));
        User writer = userHelperService.findValidUser(userId);

        campaign.setProductPrice(request.getProductPrice());

        if(Objects.equals(request.getPipelineStatusId(), PipelineStatusConstants.APPROVED)){
            campaign.confirmCampaign();
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
        campaignRepository.save(campaign);

        Pipeline pipeline = Pipeline.builder()
                .pipelineStep(pipelineStep)
                .pipelineStatus(pipelineStatus)
                .campaign(campaign)
                .name(request.getName())
                .requestAt(request.getRequestAt())
                .startedAt(request.getStartedAt())
                .endedAt(request.getEndedAt())
                .presentedAt(request.getPresentedAt())
                .content(request.getContent())
                .notes(request.getNotes())
                .writer(writer)
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

        /* 파일 저장*/
        if(files != null && !files.isEmpty()){
            // 1. 파일 S3에 올리고 돌려 받기
            List<File> fileList = fileService.uploadFile(files);
            fileList.forEach(file -> file.setPipeline(pipeline));

            // 2. 파일 DB에 저장하기
            fileService.saveFile(fileList);
        }

        pipeInfClientManagerService.saveClientManager(clientManager, pipeline);
        pipeInfClientManagerService.saveInfluencerRevenue(request.getInfluencerList(), pipeline);
        pipeUserService.saveUserList(request.getUserId(), pipeline);

    }

    @Transactional
    public void updateRevenue(Long userId, UpdateRevenueRequest request, List<MultipartFile> files){
        if(Objects.equals(request.getPipelineStatusId(), PipelineStatusConstants.APPROVED)){
            Pipeline existPipeline = pipelineRepository.findApprovePipeline(
                    request.getCampaignId(),
                    PipelineStepConstants.REVENUE,
                    PipelineStatusConstants.APPROVED
            );

            if(existPipeline != null && !Objects.equals(existPipeline.getPipelineId(), request.getPipelineId())){
                throw new BusinessException(CampaignErrorCode.APPROVED_REVENUE_ALREADY_EXISTS);
            }
        }
        Campaign campaign = campaignHelperService.findValidCampaign(request.getCampaignId());
        /* 이미 완료된 캠페인인 경우 수정 불가 */
        if(Objects.equals(campaign.getCampaignStatus().getCampaignStatusId(), CampaignStatusConstants.FINISHED)){
            throw new BusinessException(CampaignErrorCode.FINISHED_CAMPAIGN);
        }

        ClientManager clientManager = clientHelperService.findValidClientManager(request.getClientManagerId());
        PipelineStatus pipelineStatus = pipelineStatusRepository.findById(request.getPipelineStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND));
        User writer = userHelperService.findValidUser(userId);

        Pipeline foundPipeline = campaignHelperService.findValidPipeline(request.getPipelineId());

        if(!Objects.equals(foundPipeline.getPipelineStep().getPipelineStepId(), PipelineStepConstants.REVENUE)){
            throw new BusinessException(CampaignErrorCode.INVALID_ACCESS);
        }

        if(Objects.equals(request.getPipelineStatusId(), PipelineStatusConstants.APPROVED)){
            campaign.confirmCampaign();
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


        /* 관련 picm 찾아 주기 */
        dashboardHelperService.deleteInfluencerTrafficByPicmId(pipeInfClientManagerService.findPicmByPipeline(foundPipeline));

        /* 연관 테이블 지워주기 */
        campaignHelperService.deleteRelationTable(foundPipeline);

        /* 파일 테이블 지워주기 */
        fileService.deleteByPipeline(foundPipeline, request.getExistingFileList());

        campaign.updateRevenue(request.getProductPrice(), request.getSalesQuantity());

        foundPipeline.updateRevenue(
                pipelineStatus,
                campaign,
                request.getName(),
                request.getRequestAt(),
                request.getStartedAt(),
                request.getEndedAt(),
                request.getPresentedAt(),
                request.getContent(),
                request.getNotes(),
                writer
        );

        /* 파일 저장하기 */
        if(files != null && !files.isEmpty()){
            // 1. 파일 S3에 올리고 돌려 받기
            List<File> fileList = fileService.uploadFile(files);
            fileList.forEach(file -> file.setPipeline(foundPipeline));

            // 2. 파일 DB에 저장하기
            fileService.saveFile(fileList);
        }


        pipeInfClientManagerService.saveClientManager(clientManager, foundPipeline);
        pipeInfClientManagerService.saveInfluencerRevenue(request.getInfluencerList(), foundPipeline);
        pipeUserService.saveUserList(request.getUserId(), foundPipeline);

    }

    public void deleteRevenue(Long pipelineId) {
        // 1. 삭제할 파이프라인 찾아 주기
        Pipeline foundPipeline = campaignHelperService.findValidPipeline(pipelineId);

        if(foundPipeline.getPipelineStatus().getPipelineStatusId().equals(PipelineStatusConstants.APPROVED)){
            throw new BusinessException(CampaignErrorCode.APPROVED_PIPELINE_CANNOT_BE_DELETED);
        }

        if(!Objects.equals(foundPipeline.getPipelineStep().getPipelineStepId(), PipelineStepConstants.REVENUE)){
            throw new BusinessException(CampaignErrorCode.INVALID_ACCESS);
        }

        // 2. 파이프라인 소프트 딜리트 하기
        foundPipeline.softDelete();

        // 3. 관련 테이블 지워주기
        dashboardHelperService.deleteInfluencerTrafficByPicmId(pipeInfClientManagerService.findPicmByPipeline(foundPipeline));
        campaignHelperService.deleteRelationTable(foundPipeline);

        /* 파일 테이블 지워주기 */
        fileService.deleteByPipeline(foundPipeline);
    }

    public void deleteAllRevenue(Long pipelineId) {
        // 1. 삭제할 파이프라인 찾아 주기
        Pipeline foundPipeline = campaignHelperService.findValidAllPipeline(pipelineId);

        if(foundPipeline.getPipelineStatus().getPipelineStatusId().equals(PipelineStatusConstants.APPROVED)){
            throw new BusinessException(CampaignErrorCode.APPROVED_PIPELINE_CANNOT_BE_DELETED);
        }

        if(!Objects.equals(foundPipeline.getPipelineStep().getPipelineStepId(), PipelineStepConstants.REVENUE)){
            throw new BusinessException(CampaignErrorCode.INVALID_ACCESS);
        }

        // 2. 파이프라인 소프트 딜리트 하기
        foundPipeline.softDelete();

        // 3. 관련 테이블 지워주기
        dashboardHelperService.deleteInfluencerTrafficByPicmId(pipeInfClientManagerService.findPicmByPipeline(foundPipeline));
        campaignHelperService.deleteRelationTable(foundPipeline);

        /* 파일 테이블 지워주기 */
        fileService.deleteByPipeline(foundPipeline);
    }
}
