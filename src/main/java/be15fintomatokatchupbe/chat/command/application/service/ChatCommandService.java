package be15fintomatokatchupbe.chat.command.application.service;

import be15fintomatokatchupbe.chat.command.application.dto.response.CreateChatRoomResponse;
import be15fintomatokatchupbe.chat.command.application.dto.response.ExitChatRoomResponse;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRepository userChatRepository;

    @Transactional
    public CreateChatRoomResponse createChatRoom(Long creatorId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            throw new BusinessException(ChatErrorCode.INVALID_CHAT_ROOM_REQUEST);
        }

        Set<Long> participants = new HashSet<>(userIds);
        participants.add(creatorId);

        if (participants.size() == 1) {
            throw new BusinessException(ChatErrorCode.SINGLE_PARTICIPANT_NOT_ALLOWED);
        }

        Chat chatRoom = chatRoomRepository.save(new Chat());

        for (Long userId : participants) {
            UserChat userChat = UserChat.builder()
                    .chatId(chatRoom.getChatId())
                    .userId(userId)
                    .isDeleted(StatusType.N)
                    .build();
            userChatRepository.save(userChat);
        }

        return new CreateChatRoomResponse(chatRoom.getChatId(), "채팅방이 생성되었습니다.");
    }
    @Transactional
    public ExitChatRoomResponse exitRoom(Long userId, Long chatId)
    {
        userChatRepository.findAll().forEach(uc -> {
        });

        UserChat userChat = userChatRepository.findByUserIdAndChatId(userId, chatId)
                .orElseThrow(() -> {
                    return new BusinessException(ChatErrorCode.CHAT_ROOM_NOT_FOUND);
                });

        if (userChat.getIsDeleted() == StatusType.Y) {
            throw new BusinessException(ChatErrorCode.ALREADY_EXITED_CHAT);
        }

        userChat.setIsDeleted(StatusType.Y);
        userChatRepository.save(userChat);

        return new ExitChatRoomResponse("채팅방에서 나갔습니다.");
    }
}
