package be15fintomatokatchupbe.campaign.command.application.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.IdeaRequest;
import be15fintomatokatchupbe.campaign.command.application.support.CampaignHelperService;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Idea;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStep;
import be15fintomatokatchupbe.campaign.command.domain.repository.IdeaRepository;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.notification.command.application.service.FcmService;
import be15fintomatokatchupbe.notification.command.domain.repository.NotificationRepository;
import be15fintomatokatchupbe.notification.command.domain.repository.SseEmitterRepository;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdeaCommandServiceTest {

    private IdeaRepository ideaRepository;
    private CampaignHelperService campaignHelperService;
    private UserRepository userRepository;
    private FcmService fcmService;
    private NotificationRepository notificationRepository;
    private SseEmitterRepository sseEmitterRepository;

    private IdeaCommandService ideaCommandService;

    @BeforeEach
    void setUp() {
        ideaRepository = mock(IdeaRepository.class);
        campaignHelperService = mock(CampaignHelperService.class);
        userRepository = mock(UserRepository.class);
        fcmService = mock(FcmService.class);
        notificationRepository = mock(NotificationRepository.class);
        sseEmitterRepository = mock(SseEmitterRepository.class);

        ideaCommandService = new IdeaCommandService(
                ideaRepository,
                campaignHelperService,
                userRepository,
                fcmService,
                notificationRepository,
                sseEmitterRepository
        );
    }

    @Test
    void createIdea_success() {
        User user = User.builder().userId(1L).name("홍길동").fcmToken("token").build();
        IdeaRequest request = IdeaRequest.builder()
                .pipeline(1L)
                .content("좋은 아이디어입니다")
                .build();

        PipelineStep step = PipelineStep.builder().pipelineStepId(3L).build();
        Pipeline pipeline = Pipeline.builder().pipelineId(1L).pipelineStep(step).build();

        when(campaignHelperService.findValidPipeline(1L)).thenReturn(pipeline);
        when(userRepository.findAllByIsDeleted(StatusType.N)).thenReturn(List.of(user));
        when(sseEmitterRepository.get(any())).thenReturn(Optional.empty());

        ideaCommandService.createIdea(user, request);

        verify(ideaRepository, times(1)).save(any(Idea.class));
        verify(notificationRepository, times(0)).save(any());
        verify(fcmService, times(0)).sendMessage(any(), any(), any());
    }

    @Test
    void createIdea_blankContent_shouldThrowException() {
        User user = User.builder().userId(1L).name("홍길동").build();
        IdeaRequest request = IdeaRequest.builder()
                .pipeline(1L)
                .content("   ")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ideaCommandService.createIdea(user, request);
        });

        assertEquals(CampaignErrorCode.IDEA_IS_BLANK, exception.getErrorCode());
    }

    @Test
    void deleteIdea_success() {
        User user = User.builder().userId(1L).build();
        Idea idea = Idea.builder()
                .ideaId(100L)
                .user(user)
                .isDeleted(StatusType.N)
                .build();

        when(ideaRepository.findById(100L)).thenReturn(Optional.of(idea));

        ideaCommandService.deleteIdea(user, 100L);

        assertEquals(StatusType.Y, idea.getIsDeleted());
    }

    @Test
    void deleteIdea_ideaNotFound_shouldThrowException() {
        User user = User.builder().userId(1L).build();
        when(ideaRepository.findById(100L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ideaCommandService.deleteIdea(user, 100L);
        });

        assertEquals(CampaignErrorCode.IDEA_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void deleteIdea_invalidUser_shouldThrowException() {
        User user = User.builder().userId(1L).build();
        User otherUser = User.builder().userId(2L).build();
        Idea idea = Idea.builder()
                .ideaId(100L)
                .user(otherUser)
                .isDeleted(StatusType.N)
                .build();

        when(ideaRepository.findById(100L)).thenReturn(Optional.of(idea));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ideaCommandService.deleteIdea(user, 100L);
        });

        assertEquals(CampaignErrorCode.INVALID_USER, exception.getErrorCode());
    }

    @Test
    void deleteIdea_alreadyDeleted_shouldThrowException() {
        User user = User.builder().userId(1L).build();
        Idea idea = Idea.builder()
                .ideaId(100L)
                .user(user)
                .isDeleted(StatusType.Y)
                .build();

        when(ideaRepository.findById(100L)).thenReturn(Optional.of(idea));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ideaCommandService.deleteIdea(user, 100L);
        });

        assertEquals(CampaignErrorCode.DELETED_IDEA, exception.getErrorCode());
    }
}
