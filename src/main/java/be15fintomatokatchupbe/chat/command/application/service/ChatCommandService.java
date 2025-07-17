package be15fintomatokatchupbe.chat.command.application.service;

import be15fintomatokatchupbe.chat.command.application.dto.request.ChatInviteRequest;
import be15fintomatokatchupbe.chat.command.application.dto.request.CreateChatRoomRequest;
import be15fintomatokatchupbe.chat.command.application.dto.response.CreateChatRoomResponse;
import be15fintomatokatchupbe.chat.command.application.dto.response.ExitChatRoomResponse;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Chat;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.UserChat;
import be15fintomatokatchupbe.chat.command.domain.repository.ChatRoomRepository;
import be15fintomatokatchupbe.chat.command.domain.repository.UserChatRepository;
import be15fintomatokatchupbe.chat.exception.ChatErrorCode;
import be15fintomatokatchupbe.chat.query.application.dto.response.UserSimpleDto;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.user.query.mapper.UserQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRepository userChatRepository;
    private final UserQueryMapper userQueryMapper;

    @Transactional
    public CreateChatRoomResponse createChatRoom(CreateChatRoomRequest request) {
        Long creatorId = request.getUserId();
        List<Long> userIds = request.getUserIds();

        if (userIds == null) {
            throw new BusinessException(ChatErrorCode.INVALID_CHAT_ROOM_REQUEST);
        }
        Set<Long> participants = new HashSet<>(userIds);
        participants.add(creatorId);

        if (participants.size() <= 1) {
            throw new BusinessException(ChatErrorCode.SINGLE_PARTICIPANT_NOT_ALLOWED);
        }


        String chatName = request.getChatName();
        if (chatName == null || chatName.isBlank()) {
            List<UserSimpleDto> users = userQueryMapper.findUserNamesByIds(participants);
            chatName = users.stream()
                    .map(UserSimpleDto::getUserName)
                    .collect(Collectors.joining(", "));
        }

        Chat chatRoom = chatRoomRepository.save(Chat.builder()
                .chatName(chatName)
                .createdAt(LocalDateTime.now())
                .build());

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
    public ExitChatRoomResponse exitRoom(Long userId, Long chatId) {

        UserChat userChat = userChatRepository.findByUserIdAndChatIdAndIsDeleted(userId, chatId, StatusType.N)
                .orElseThrow(() -> {
                    return new BusinessException(ChatErrorCode.CHAT_ROOM_NOT_FOUND);
                });

        userChat.setIsDeleted(StatusType.Y);
        userChatRepository.save(userChat);

        return new ExitChatRoomResponse("채팅방에서 나갔습니다.");
    }

    @Transactional
    public void inviteChatMembers(Long chatRoomId, ChatInviteRequest request) {
        Long inviterId = request.getUserId();
        List<Long> invitedUserIds = request.getInvitedUserIds();

        Chat chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        if (invitedUserIds == null || invitedUserIds.isEmpty()) {
            throw new BusinessException(ChatErrorCode.INVALID_CHAT_ROOM_REQUEST);
        }

        List<Long> foundUserIds = userQueryMapper.findUserIdsByIds(invitedUserIds);
        if (foundUserIds.isEmpty()) {
            throw new BusinessException(ChatErrorCode.USER_NOT_FOUND);
        }

        for (Long inviteeId : foundUserIds) {
            Optional<UserChat> existingUserChatOptional = userChatRepository.findByUserIdAndChatId(inviteeId, chatRoomId);

            if (existingUserChatOptional.isPresent()) {
                UserChat existingUserChat = existingUserChatOptional.get();

                if (existingUserChat.getIsDeleted() == StatusType.Y) {
                    existingUserChat.setIsDeleted(StatusType.N);
                    userChatRepository.save(existingUserChat);
                } else {
                }
            } else {
                UserChat newUserChat = UserChat.builder()
                        .chatId(chatRoomId)
                        .userId(inviteeId)
                        .isDeleted(StatusType.N)
                        .build();
                userChatRepository.save(newUserChat);
            }
        }
    }
}