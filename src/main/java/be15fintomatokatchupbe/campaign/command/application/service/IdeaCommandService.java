package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Idea;
import be15fintomatokatchupbe.campaign.command.domain.repository.IdeaRepository;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdeaCommandService {
    private final IdeaRepository ideaRepository;

    @Transactional
    public void deleteQuotationIdea(Long userId, Long quotationIdeaId) {
        Idea idea = ideaRepository.findByIdAndUserIdAndIsDeleted(quotationIdeaId, userId, StatusType.N)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.IDEA_ACCESS_DENIED));
        idea.softDelete();

    }

}