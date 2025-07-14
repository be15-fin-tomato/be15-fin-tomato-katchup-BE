package be15fintomatokatchupbe.chat.query.application.service;

import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import be15fintomatokatchupbe.chat.command.domain.repository.MessageMongoRepository;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatParticipantDto;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomResponse;
import be15fintomatokatchupbe.chat.query.application.dto.response.MessageResponse;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomDetailResponse;
import be15fintomatokatchupbe.chat.query.application.mapper.ChatRoomQueryMapper;
import be15fintomatokatchupbe.chat.query.application.dto.response.UserSimpleDto;
import be15fintomatokatchupbe.user.query.mapper.UserQueryMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatQueryServiceTest {

    @Mock
    private ChatRoomQueryMapper chatRoomMapper;

    @Mock
    private MessageMongoRepository messageMongoRepository;

    @Mock
    private UserQueryMapper userQueryMapper;

    @InjectMocks
    private ChatQueryService chatQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getChatRoomsWithLastMessage_success() {
        Long userId = 1L;

        ChatRoomResponse mockRoom = ChatRoomResponse.builder()
                .chatId(10L)
                .name("테스트방")
                .build();

        Message lastMessage = Message.builder()
                .chatId(10L)
                .senderId(2L)
                .message("안녕하세요")
                .sentAt(LocalDateTime.now())
                .readUserIds(Set.of(2L))
                .build();

        List<ChatParticipantDto> participants = List.of(new ChatParticipantDto(1L, "철수"));

        when(chatRoomMapper.findChatRoomsByUserId(userId)).thenReturn(List.of(mockRoom));
        when(messageMongoRepository.findFirstByChatIdOrderBySentAtDesc(10L)).thenReturn(Optional.of(lastMessage));
        when(chatRoomMapper.findParticipantsByChatId(10L)).thenReturn(participants);
        when(messageMongoRepository.countByChatIdAndReadUserIdsNotContaining(10L, userId)).thenReturn(1L);

        List<ChatRoomResponse> result = chatQueryService.getChatRoomsWithLastMessage(userId);

        assertEquals(1, result.size());
        assertEquals("안녕하세요", result.get(0).getLastMessage());
        assertEquals(1L, result.get(0).getUnreadCount());
    }

    @Test
    void getChatRoomDetail_success() {
        Long chatId = 10L;
        Long userId = 1L;

        Message message = Message.builder()
                .chatId(chatId)
                .senderId(2L)
                .message("테스트 메시지")
                .sentAt(LocalDateTime.now())
                .readUserIds(new HashSet<>())
                .build();

        UserSimpleDto sender = new UserSimpleDto(2L, "영희");

        when(messageMongoRepository.findByChatIdOrderBySentAtAsc(chatId)).thenReturn(List.of(message));
        when(userQueryMapper.findUserNamesByIds(Set.of(2L))).thenReturn(List.of(sender));

        ChatRoomDetailResponse response = chatQueryService.getChatRoomDetail(chatId, userId);

        assertEquals(chatId, response.getChatId());
        assertEquals(1, response.getMessages().size());
        MessageResponse msg = response.getMessages().get(0);
        assertEquals("영희", msg.getSenderName());
        assertFalse(msg.isMine());
        verify(messageMongoRepository).markMessagesAsRead(chatId, userId);
    }
}
