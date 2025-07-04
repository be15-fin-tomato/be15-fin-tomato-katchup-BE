package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.IdeaRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Idea;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.campaign.command.domain.repository.IdeaRepository;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.notification.command.application.service.FcmService;
import be15fintomatokatchupbe.notification.command.domain.aggregate.Notification;
import be15fintomatokatchupbe.notification.command.domain.repository.NotificationRepository;
import be15fintomatokatchupbe.notification.command.domain.repository.SseEmitterRepository;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdeaCommandService {
    private final IdeaRepository ideaRepository;
    private final CampaignHelperService campaignHelperService;
    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;
    private final SseEmitterRepository sseEmitterRepository;

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

        String title = "파이프라인 내 새로운 의견 등록";
        String body = user.getName() + "님이 새로운 의견을 등록했습니다.";

        List<User> allUsers = userRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        for (User target : allUsers) {
            if (!target.getUserId().equals(user.getUserId())) {

                // FCM 전송
                String token = target.getFcmToken();
                if (token != null && !token.isBlank()) {
                    fcmService.sendMessage(token, title, body);
                }

                // Notification 저장
                Notification notification = Notification.builder()
                        .userId(target.getUserId())
                        .notificationTypeId(3L)
                        .notificationContent(body)
                        .getTime(now)
                        .build();

                notificationRepository.save(notification);

                sseEmitterRepository.get(target.getUserId()).ifPresent(emitter -> {
                    try {
                        emitter.send(SseEmitter.event()
                                .name("new-notification")
                                .data(body));
                    } catch (IOException e) {
                        sseEmitterRepository.delete(target.getUserId());
                    }
                });

            }
        }
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