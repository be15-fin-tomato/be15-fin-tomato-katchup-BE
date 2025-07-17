package be15fintomatokatchupbe.chat.command.domain.aggregate.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    private String id;
    private Long chatId;
    private Long senderId;

    private String message;
    private String fileUrl;
    private LocalDateTime sentAt;
    private Set<Long> readUserIds = new HashSet<>();
}
