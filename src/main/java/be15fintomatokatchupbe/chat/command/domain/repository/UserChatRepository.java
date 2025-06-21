package be15fintomatokatchupbe.chat.command.domain.repository;

import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {}
