package be15fintomatokatchupbe.chat.command.domain.repository;

import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.UserChat;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    @Query("SELECT uc FROM UserChat uc WHERE uc.userId = :userId AND uc.chatId = :chatId")
    Optional<UserChat> findByUserIdAndChatId(@Param("userId") Long userId,
                                             @Param("chatId") Long chatId);

}