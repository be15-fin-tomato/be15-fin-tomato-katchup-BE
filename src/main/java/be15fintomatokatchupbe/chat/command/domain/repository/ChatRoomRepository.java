package be15fintomatokatchupbe.chat.command.domain.repository;


import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<Chat, Long> {

    Chat findByChatId(Long chatId);
}