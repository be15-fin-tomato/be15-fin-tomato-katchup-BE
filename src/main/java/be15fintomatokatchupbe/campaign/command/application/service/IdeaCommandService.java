package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.IdeaRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Idea;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.campaign.command.domain.repository.IdeaRepository;
import be15fintomatokatchupbe.campaign.command.domain.repository.PipelineRepository;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdeaCommandService {
    private final IdeaRepository ideaRepository;
    private final CampaignHelperService campaignHelperService;

    @Transactional
    public void createIdea(User user, IdeaRequest request) {

        if(request.getContent() == null || request.getContent().isBlank()){
            throw new BusinessException(CampaignErrorCode.IDEA_IS_BLANK);
        }

        Pipeline pipeline = campaignHelperService.findValidPipeline(request.getPipeline());

        Idea idea = Idea.builder()
                .pipeline(pipeline)
                .content(request.getContent())
                .user(user)
                .build();

        ideaRepository.save(idea);
    }

    @Transactional
    public void deleteIdea(User user, Long ideaId) {
        // 삭제 여부 관계 없이 먼저 찾고
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.IDEA_NOT_FOUND));

        // 사용자 불일치
        if (!idea.getUser().getUserId().equals(user.getUserId())) {
            throw new BusinessException(CampaignErrorCode.INVALID_USER);
        }

        // 이미 삭제된 의견
        if (idea.getIsDeleted() == StatusType.Y) {
            throw new BusinessException(CampaignErrorCode.DELETED_IDEA);
        }

        idea.softDelete();
    }
}