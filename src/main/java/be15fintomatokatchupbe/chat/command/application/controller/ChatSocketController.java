package be15fintomatokatchupbe.chat.command.application.controller;

import be15fintomatokatchupbe.chat.command.application.dto.response.ChatResponseDTO;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Chat;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import be15fintomatokatchupbe.chat.command.domain.repository.ChatRoomRepository;
import be15fintomatokatchupbe.chat.command.domain.repository.MessageRepository;
import be15fintomatokatchupbe.chat.exception.ChatErrorCode;
import be15fintomatokatchupbe.chat.query.application.mapper.ChatRoomQueryMapper;
import be15fintomatokatchupbe.chat.query.application.mapper.UserChatMapper;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.notification.command.application.service.FcmService;
import be15fintomatokatchupbe.notification.command.domain.aggregate.Notification;
import be15fintomatokatchupbe.notification.command.domain.repository.NotificationRepository;
import be15fintomatokatchupbe.notification.command.domain.repository.SseEmitterRepository;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Tag(name = "채팅")
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatRoomQueryMapper chatRoomQueryMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;
    private final UserChatMapper userChatMapper;
    private final UserRepository userRepository;
    private final SseEmitterRepository sseEmitterRepository;


    @Operation(summary = "채팅 보내기", description = "사용자는 채팅방에서 다른 사용자에게 메시지를 보낼 수 있다.")
    @Transactional
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Message message,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {

        Long userId = (Long) sessionAttributes.get("userId");
        Long chatId = message.getChatId();

        if (userId == null) {
            throw new BusinessException(ChatErrorCode.UNAUTHORIZED_CHAT_ACCESS);
        }

        boolean isParticipant = chatRoomQueryMapper.existsByChatIdAndUserId(chatId, userId);
        if (!isParticipant) {
            throw new BusinessException(ChatErrorCode.NOT_CHAT_MEMBER);
        }

        message.setSenderId(userId);
        message.setSentAt(LocalDateTime.now());
        message.setReadUserIds(Set.of(userId));

        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/room." + message.getChatId(), message);

        User user = userRepository.findByUserId(userId);
        String userName = user.getName();

        // 메시지 or 첨부파일 미리보기
        String preview = message.getFileUrl() != null ? "[파일 첨부]" : message.getMessage();
        notifyOtherParticipants(message.getChatId(), message.getSenderId(), preview, userName);
    }

    /* fireBase 웹푸시 알림 요청 */
    private void notifyOtherParticipants(Long chatId, Long senderId, String message, String userName) {
        List<ChatResponseDTO> targets  = userChatMapper.findFcmTokensByChatId(chatId, senderId);

        LocalDateTime now = LocalDateTime.now();

        for (ChatResponseDTO dto : targets ) {
            Long receiverId = dto.getUserId();
            String token = dto.getFcmToken();

            if (token != null && !token.isBlank()) {
                String title = userName + "님이 새로운 채팅을 보냈습니다.";
                fcmService.sendMessage(token, title, message);
            }

            Chat chat = chatRoomRepository.findByChatId(chatId);

            String messages = chat.getChatName() + "에서 새로운 채팅이 있습니다.";

            Notification notification = Notification.builder()
                    .userId(receiverId)
                    .notificationTypeId(5L)
                    .notificationContent(messages)
                    .getTime(now)
                    .build();

            notificationRepository.save(notification);

            sseEmitterRepository.get(receiverId).ifPresent(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("new-notification")
                            .data(messages));
                } catch (IOException e) {
                    sseEmitterRepository.delete(receiverId);
                }
            });
        }
    }
}
