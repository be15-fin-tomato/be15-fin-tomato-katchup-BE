package be15fintomatokatchupbe.chat.command.application.controller;

import be15fintomatokatchupbe.chat.command.application.dto.response.ChatResponseDTO;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import be15fintomatokatchupbe.chat.command.domain.repository.MessageRepository;
import be15fintomatokatchupbe.chat.exception.ChatErrorCode;
import be15fintomatokatchupbe.chat.query.application.mapper.ChatRoomQueryMapper;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.chat.command.domain.repository.UserChatRepository;
import be15fintomatokatchupbe.notification.command.application.service.FcmService;
import be15fintomatokatchupbe.notification.command.domain.aggregate.Notification;
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
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatRoomQueryMapper chatRoomQueryMapper;
    private final UserChatRepository userChatRepository;
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;


    @Transactional
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Message message,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {

        Long userId = (Long) sessionAttributes.get("userId");
        Long chatId = message.getChatId();

        System.out.println(">>> userId: " + userId);
        System.out.println(">>> 받은 chatId: " + chatId);
        System.out.println(">>> 받은 message: " + message.getMessage());

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
