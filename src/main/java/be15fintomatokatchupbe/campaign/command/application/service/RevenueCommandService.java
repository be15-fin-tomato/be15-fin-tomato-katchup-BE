package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateRevenueRequest;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevenueCommandService {
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


        ClientManager clientManager = clientHelperService.findValidClientManager(request.getClientManagerId());
        Campaign campaign = campaignHelperService.findValidCampaign(request.getCampaignId());
        PipelineStep pipelineStep = pipelineStepRepository.findById(PipelineStepConstants.REVENUE)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STEP_NOT_FOUND));
        PipelineStatus pipelineStatus = pipelineStatusRepository.findById(request.getPipelineStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.PIPELINE_STATUS_NOT_FOUND));
        User writer = userHelperService.findValidUser(userId);

        campaign.setProductPrice(request.getProductPrice());
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
}
