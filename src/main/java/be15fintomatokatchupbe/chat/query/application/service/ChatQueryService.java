package be15fintomatokatchupbe.chat.query.application.service;

import be15fintomatokatchupbe.chat.command.domain.repository.MessageMongoRepository;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatParticipantDto;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomResponse;
import be15fintomatokatchupbe.chat.query.application.mapper.ChatRoomQueryMapper;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatRoomQueryMapper chatRoomMapper;
    private final MessageMongoRepository messageMongoRepository;

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

}
