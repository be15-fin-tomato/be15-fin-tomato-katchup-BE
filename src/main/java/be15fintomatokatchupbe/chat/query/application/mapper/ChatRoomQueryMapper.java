package be15fintomatokatchupbe.chat.query.application.mapper;

import be15fintomatokatchupbe.chat.query.application.dto.response.ChatParticipantDto;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatRoomQueryMapper {
    /* 채팅방 목록*/
    List<ChatRoomResponse> findChatRoomsByUserId(@Param("userId") Long userId);
    /* 채팅방 사람들 초대하기*/
    List<ChatParticipantDto> findParticipantsByChatId(@Param("chatId") Long chatId);

    boolean existsByChatIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);
    /* 채팅방 검색 ( 채팅방 이름 , 속한 사람 1명 )*/
    List<ChatRoomResponse> searchChatRoomsByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);

}
