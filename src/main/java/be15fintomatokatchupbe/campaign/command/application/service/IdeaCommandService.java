package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Idea;
import be15fintomatokatchupbe.campaign.command.domain.repository.IdeaRepository;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdeaCommandService {
    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;

    @Transactional
    public void deleteQuotationIdea(User user, Long ideaId) {
        Idea idea = ideaRepository.findByIdeaIdAndUserAndIsDeleted(ideaId, user, StatusType.N)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.IDEA_ACCESS_DENIED));
        idea.softDelete();
    }
}