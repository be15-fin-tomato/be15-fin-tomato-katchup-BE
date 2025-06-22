package be15fintomatokatchupbe.chat.command.domain.aggregate.entity;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_id")
    private Long userChatId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name="is_deleted", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isDeleted = StatusType.N;

}
