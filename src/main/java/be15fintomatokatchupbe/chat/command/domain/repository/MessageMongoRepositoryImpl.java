package be15fintomatokatchupbe.chat.command.domain.repository;

import be15fintomatokatchupbe.chat.command.domain.aggregate.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageMongoRepositoryImpl implements MessageMongoRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void markMessagesAsRead(Long chatId, Long userId) {
        Query query = new Query(
                Criteria.where("chatId").is(chatId)
                        .and("readUserIds").ne(userId)
        );

        Update update = new Update().addToSet("readUserIds", userId);

        mongoTemplate.updateMulti(query, update, Message.class);
    }
}