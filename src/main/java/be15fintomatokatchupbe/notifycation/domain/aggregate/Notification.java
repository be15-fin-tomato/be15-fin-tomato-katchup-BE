package be15fintomatokatchupbe.notifycation.domain.aggregate;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.sql.Timestamp;

@Entity
@Table(name = "notification")
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long userId;

    private Long notificationTypeId;

    private String notificationContent;

    private StatusType isRead = StatusType.N;

    private Timestamp getTime;

    private StatusType isDeleted = StatusType.N;
}
