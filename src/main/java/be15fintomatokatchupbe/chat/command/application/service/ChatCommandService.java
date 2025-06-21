package be15fintomatokatchupbe.chat.command.application.service;

import be15fintomatokatchupbe.chat.command.application.dto.response.CreateChatRoomResponse;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Chat;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.UserChat;
import be15fintomatokatchupbe.chat.command.domain.repository.ChatRoomRepository;
import be15fintomatokatchupbe.chat.command.domain.repository.UserChatRepository;
import be15fintomatokatchupbe.chat.exception.ChatErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRepository userChatRepository;

    @Transactional
    public CreateChatRoomResponse createChatRoom(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            throw new BusinessException(ChatErrorCode.INVALID_CHAT_ROOM_REQUEST);
        }

        if (userIds.size() == 1) {
            throw new BusinessException(ChatErrorCode.SINGLE_PARTICIPANT_NOT_ALLOWED);
        }
        Chat chatRoom = chatRoomRepository.save(new Chat());

        for (Long userId : userIds) {
            UserChat userChat = UserChat.builder()
                    .chatId(chatRoom.getChatId())
                    .userId(userId)
                    .isDeleted(StatusType.N)
                    .build();
            userChatRepository.save(userChat);
        }

        return new CreateChatRoomResponse(chatRoom.getChatId(), "채팅방이 생성되었습니다.");
    }
}
