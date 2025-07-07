package be15fintomatokatchupbe.chat.command.domain.repository;

public interface MessageMongoRepositoryCustom {
    void markMessagesAsRead(Long chatId, Long userId);
}
