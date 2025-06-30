package be15fintomatokatchupbe.chat.query.application.mapper;

import be15fintomatokatchupbe.chat.query.application.dto.response.ChatParticipantDto;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatRoomQueryMapper {
    List<ChatRoomResponse> findChatRoomsByUserId(@Param("userId") Long userId);

    List<ChatParticipantDto> findParticipantsByChatId(@Param("chatId") Long chatId);

    boolean existsByChatIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);

}
