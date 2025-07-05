package be15fintomatokatchupbe.notification.query.dto;

import be15fintomatokatchupbe.common.domain.StatusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class NotificationsAllDTO {

    private Long notificationId;
    private String notificationContent;
    private Long notificationTypeId;
    private StatusType isRead;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime getTime;
    private Long targetId;

}
