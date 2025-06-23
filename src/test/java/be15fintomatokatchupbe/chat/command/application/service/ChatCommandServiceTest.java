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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatCommandServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserChatRepository userChatRepository;

    @InjectMocks
    private ChatCommandService chatCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createChatRoom_success() {
        Long creatorId = 1L;
        List<Long> userIds = Arrays.asList(2L, 3L);

        Chat savedChat = Chat.builder().chatId(100L).build();
        when(chatRoomRepository.save(any())).thenReturn(savedChat);

        CreateChatRoomResponse response = chatCommandService.createChatRoom(creatorId, userIds);

        assertNotNull(response);
        assertEquals(100L, response.getChatId());
        assertEquals("채팅방이 생성되었습니다.", response.getMessage());

        verify(chatRoomRepository, times(1)).save(any());
        verify(userChatRepository, times(3)).save(any(UserChat.class));
    }

    @Test
    void createChatRoom_fail_when_userIds_null() {
        Long creatorId = 1L;

        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatCommandService.createChatRoom(creatorId, null));

        assertEquals(ChatErrorCode.INVALID_CHAT_ROOM_REQUEST, ex.getErrorCode());
    }

    @Test
    void createChatRoom_fail_when_only_creator() {
        Long creatorId = 1L;
        List<Long> userIds = List.of(1L);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatCommandService.createChatRoom(creatorId, userIds));

        assertEquals(ChatErrorCode.SINGLE_PARTICIPANT_NOT_ALLOWED, ex.getErrorCode());
    }


    // 퇴장 테스트
    @Test
    void exitRoom_success() {
        Long userId = 1L;
        Long chatId = 1L;

        UserChat userChat = UserChat.builder()
                .userChatId(10L)
                .userId(userId)
                .chatId(chatId)
                .isDeleted(StatusType.N)
                .build();

        when(userChatRepository.findByUserIdAndChatId(userId, chatId)).thenReturn(Optional.of(userChat));

        ExitChatRoomResponse response = chatCommandService.exitRoom(userId, chatId);

        assertEquals("채팅방에서 나갔습니다.", response.getMessage());
        assertEquals(StatusType.Y, userChat.getIsDeleted());
        verify(userChatRepository).save(userChat);
    }

    @Test
    void exitRoom_fail_when_userChat_not_found() {
        Long userId = 1L;
        Long chatId = 999L;

        when(userChatRepository.findByUserIdAndChatId(userId, chatId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatCommandService.exitRoom(userId, chatId));

        assertEquals(ChatErrorCode.CHAT_ROOM_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void exitRoom_fail_when_already_exited() {
        Long userId = 1L;
        Long chatId = 1L;

        UserChat userChat = UserChat.builder()
                .userChatId(10L)
                .userId(userId)
                .chatId(chatId)
                .isDeleted(StatusType.Y)
                .build();

        when(userChatRepository.findByUserIdAndChatId(userId, chatId)).thenReturn(Optional.of(userChat));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatCommandService.exitRoom(userId, chatId));

        assertEquals(ChatErrorCode.ALREADY_EXITED_CHAT, ex.getErrorCode());
    }
}
