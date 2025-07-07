package be15fintomatokatchupbe.chat.query.application.service;

import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import be15fintomatokatchupbe.chat.command.domain.repository.MessageMongoRepository;
import be15fintomatokatchupbe.chat.query.application.dto.response.*;
import be15fintomatokatchupbe.chat.query.application.mapper.ChatRoomQueryMapper;
import be15fintomatokatchupbe.user.query.mapper.UserQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatRoomQueryMapper chatRoomMapper;
    private final MessageMongoRepository messageMongoRepository;
    private final UserQueryMapper userQueryMapper;

    public List<ChatRoomResponse> getChatRoomsWithLastMessage(Long userId) {
        return getChatRoomsWithLastMessage(userId, null);
    }

    public List<ChatRoomResponse> getChatRoomsWithLastMessage(Long userId, String keyword) {
        List<ChatRoomResponse> chatRooms;

        if (keyword == null || keyword.isBlank()) {
            chatRooms = chatRoomMapper.findChatRoomsByUserId(userId);
        } else {
            chatRooms = chatRoomMapper.searchChatRoomsByUserIdAndKeyword(userId, keyword);
        }

        return chatRooms.stream()
                .map(chatRoom -> {
                    Message lastMessage = messageMongoRepository
                            .findFirstByChatIdOrderBySentAtDesc(chatRoom.getChatId())
                            .orElse(null);

                    List<ChatParticipantDto> participants =
                            chatRoomMapper.findParticipantsByChatId(chatRoom.getChatId());

                    long unreadCount = messageMongoRepository
                            .countByChatIdAndReadUserIdsNotContaining(chatRoom.getChatId(), userId);

                    return ChatRoomResponse.builder()
                            .chatId(chatRoom.getChatId())
                            .name(chatRoom.getName())
                            .lastMessage(lastMessage != null ? lastMessage.getMessage() : null)
                            .lastSentAt(lastMessage != null ? lastMessage.getSentAt() : null)
                            .participants(participants)
                            .unreadCount(unreadCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatRoomDetailResponse getChatRoomDetail(Long chatId, Long userId) {
        List<Message> messages = messageMongoRepository.findByChatIdOrderBySentAtAsc(chatId);

        Set<Long> senderIds = messages.stream()
                .map(Message::getSenderId)
                .collect(Collectors.toSet());

        List<UserSimpleDto> users = userQueryMapper.findUserNamesByIds(senderIds);
        Map<Long, String> senderNameMap = users.stream()
                .collect(Collectors.toMap(UserSimpleDto::getUserId, UserSimpleDto::getUserName));

        List<MessageResponse> messageResponses = messages.stream()
                .map(msg -> MessageResponse.builder()
                        .senderId(msg.getSenderId())
                        .senderName(senderNameMap.getOrDefault(msg.getSenderId(), "알 수 없음"))
                        .message(msg.getMessage())
                        .sentAt(msg.getSentAt())
                        .isMine(msg.getSenderId().equals(userId))
                        .build())
                .toList();

        messageMongoRepository.markMessagesAsRead(chatId, userId);

        return ChatRoomDetailResponse.builder()
                .chatId(chatId)
                .messages(messageResponses)
                .unreadCount(0L)
                .build();
    }
}
