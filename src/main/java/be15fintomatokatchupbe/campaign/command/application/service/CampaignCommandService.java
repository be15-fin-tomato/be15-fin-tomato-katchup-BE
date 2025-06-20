package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateChanceRequest;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Campaign;
import be15fintomatokatchupbe.campaign.command.domain.repository.CampaignRepository;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class CampaignCommandService {
    private final CampaignRepository campaignRepository;

    public void createChance(Long userId, CreateChanceRequest request) {

        // 1. 캠페인 만들기

        // 2. 파이프라인 만들기

        // 3. 광고 담당자 입력하기

        // 4. 담당자 입력하기

    }
}
