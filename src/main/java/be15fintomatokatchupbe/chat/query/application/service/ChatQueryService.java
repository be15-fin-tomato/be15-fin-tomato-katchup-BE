package be15fintomatokatchupbe.chat.query.application.service;

import be15fintomatokatchupbe.chat.command.domain.repository.MessageMongoRepository;
import be15fintomatokatchupbe.chat.query.application.dto.response.*;
import be15fintomatokatchupbe.chat.query.application.mapper.ChatRoomQueryMapper;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import be15fintomatokatchupbe.user.query.mapper.UserQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatRoomQueryMapper chatRoomMapper;
    private final MessageMongoRepository messageMongoRepository;
    private final UserQueryMapper userQueryMapper;

    public List<ChatRoomResponse> getChatRoomsWithLastMessage(Long userId) {
        List<ChatRoomResponse> chatRooms = chatRoomMapper.findChatRoomsByUserId(userId);

        return chatRooms.stream()
                .map(chatRoom -> {
                    Message lastMessage = messageMongoRepository
                            .findFirstByChatIdOrderBySentAtDesc(chatRoom.getChatId())
                            .orElse(null);

                    List<ChatParticipantDto> participants =
                            chatRoomMapper.findParticipantsByChatId(chatRoom.getChatId());

                    return ChatRoomResponse.builder()
                            .chatId(chatRoom.getChatId())
                            .lastMessage(lastMessage != null ? lastMessage.getMessage() : null)
                            .lastSentAt(lastMessage != null ? lastMessage.getSentAt() : null)
                            .participants(participants)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ChatRoomDetailResponse getChatRoomDetail(Long chatId, Long userId) {
        // 1. MongoDB에서 해당 채팅방 메시지 전체 조회 (시간 오름차순)
        List<Message> messages = messageMongoRepository.findByChatIdOrderBySentAtAsc(chatId);

        // 2. 메시지 보낸 사람 ID만 추출
        Set<Long> senderIds = messages.stream()
                .map(Message::getSenderId)
                .collect(Collectors.toSet());

        // 3. MyBatis Mapper로 사용자 닉네임 조회
        List<UserSimpleDto> users = userQueryMapper.findUserNamesByIds(senderIds);
        Map<Long, String> senderNameMap = users.stream()
                .collect(Collectors.toMap(UserSimpleDto::getUserId, UserSimpleDto::getUserName));

        // 4. 메시지를 응답 형식으로 매핑
        List<MessageResponse> messageResponses = messages.stream()
                .map(msg -> MessageResponse.builder()
                        .senderId(msg.getSenderId())
                        .senderName(senderNameMap.getOrDefault(msg.getSenderId(), "알 수 없음"))
                        .message(msg.getMessage())
                        .sentAt(msg.getSentAt())
                        .isMine(msg.getSenderId().equals(userId))
                        .build())
                .toList();

        // 5. ChatRoomDetailResponse 생성
        return ChatRoomDetailResponse.builder()
                .chatId(chatId)
                .messages(messageResponses)
                .build();
    }

}
