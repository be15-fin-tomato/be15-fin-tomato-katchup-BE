package be15fintomatokatchupbe.chat.command.domain.repository;

import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

}