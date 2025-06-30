package be15fintomatokatchupbe.chat.command.domain.repository;

import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageMongoRepository extends MongoRepository<Message, String> {

    Optional<Message> findFirstByChatIdOrderBySentAtDesc(Long chatId);

    List<Message> findByChatIdOrderBySentAtAsc(Long chatId);

    long countByChatIdAndReadUserIdsNotContaining(Long chatId, Long userId);
}
