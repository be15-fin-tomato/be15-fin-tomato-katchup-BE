package be15fintomatokatchupbe.notification.query.dto;

import be15fintomatokatchupbe.common.domain.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class NotificationsAllDTO {

    private Long notificationId;
    private Long userId;
    private Long notificationTypeId;
    private String notificationContent;
    private StatusType isRead;
    private LocalDateTime getTime;

}
