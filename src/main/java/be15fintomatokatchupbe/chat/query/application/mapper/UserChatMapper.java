package be15fintomatokatchupbe.chat.query.application.mapper;

import be15fintomatokatchupbe.chat.command.application.dto.response.ChatResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserChatMapper {
    List<ChatResponseDTO> findFcmTokensByChatId(@Param("chatId") Long chatId, @Param("senderId") Long senderId);
}