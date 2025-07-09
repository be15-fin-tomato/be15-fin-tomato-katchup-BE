package be15fintomatokatchupbe.chat.command.domain.repository;

import be15fintomatokatchupbe.chat.command.application.dto.response.ChatResponseDTO;
import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.UserChat;
import be15fintomatokatchupbe.common.domain.StatusType;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    Optional<UserChat> findByUserIdAndChatIdAndIsDeleted(Long userId, Long chatId, StatusType isDeleted);

    @Query("SELECT uc FROM UserChat uc WHERE uc.userId = :userId AND uc.chatId = :chatId")
    Optional<UserChat> findByUserIdAndChatId(@Param("userId") Long userId,
                                             @Param("chatId") Long chatId);

    @Query("SELECT uc.userId FROM UserChat uc WHERE uc.chatId = :chatId AND uc.isDeleted = 'N'")
    List<Long> findUserIdsByChatId(@Param("chatId") Long chatId);

    @Query("""
    SELECT u.fcmToken,
           u.userId
    FROM UserChat uc
    JOIN User u ON uc.userId = u.userId
    WHERE uc.chatId = :chatId
      AND uc.isDeleted = 'N'
      AND u.fcmToken IS NOT NULL
      AND u.userId <> :senderId
""")
    List<ChatResponseDTO> findFcmTokensByChatId(@Param("chatId") Long chatId, @Param("senderId") Long senderId);

}