package be15fintomatokatchupbe.chat.command.application.controller;

import be15fintomatokatchupbe.chat.command.application.dto.response.ChatResponseDTO;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import be15fintomatokatchupbe.chat.command.domain.repository.MessageRepository;
import be15fintomatokatchupbe.chat.command.domain.repository.UserChatRepository;
import be15fintomatokatchupbe.notification.command.application.service.FcmService;
import be15fintomatokatchupbe.notification.command.domain.aggregate.Notification;
import be15fintomatokatchupbe.notification.command.domain.aggregate.NotificationType;
import be15fintomatokatchupbe.notification.command.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final UserChatRepository userChatRepository;
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;


    @Transactional
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Message message,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {

        Long userId = (Long) sessionAttributes.get("userId");
        message.setSenderId(userId);
        message.setSentAt(LocalDateTime.now());

        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/room." + message.getChatId(), message);

        notifyOtherParticipants(message.getChatId(), message.getSenderId(), message.getMessage());

    }

    /* fireBase 웹푸시 알림 요청 */
    private void notifyOtherParticipants(Long chatId, Long senderId, String message) {
        List<ChatResponseDTO> targets  = userChatRepository.findFcmTokensByChatId(chatId, senderId);

        for (ChatResponseDTO dto : targets ) {
            Long receiverId = dto.userId();
            String token = dto.fcmToken();

            if (token != null && !token.isBlank()) {
                fcmService.sendMessage(token, "새 채팅", message);
            }

            Notification notification = Notification.builder()
                    .userId(receiverId)
                    .notificationTypeId(5L)
                    .notificationContent(message)
                    .build();

            notificationRepository.save(notification);
        }
    }

}
