package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateChanceRequest;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Campaign;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.CampaignStatus;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.campaign.command.domain.repository.CampaignRepository;
import be15fintomatokatchupbe.campaign.command.domain.repository.CampaignStatusRepository;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.client.command.application.service.ClientCommandService;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class CampaignCommandService {
    private final CampaignRepository campaignRepository;
    private final ClientCommandService clientCommandService;
    private final CampaignStatusRepository campaignStatusRepository;

    @Transactional
    public void createChance(Long userId, CreateChanceRequest request) {

        // 0. 외부 엔티티 가져오기
        // 고객사
        ClientCompany clientCompany =
                clientCommandService.findValidClientCompany(request.getClientCompanyId());

        // 사원
        ClientManager clientManager =
                clientCommandService.findValidClientManager(request.getClientManagerId());

        // 캠페인 상태 가져오기
        CampaignStatus campaignStatus = campaignStatusRepository.findById(request.getCampaignStatusId())
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.CAMPAIGN_STATUS_NOT_FOUND));


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
                .campaign(campaign)
                .startedAt(request.getStartedAt())

                .build();

        // 3. 광고 담당자 입력하기

        // 4. 담당자 입력하기

        // 5. 해시태그 입력하기

    }
}
