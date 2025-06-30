package be15fintomatokatchupbe.notifycation.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationCreateRequest {

    private Long notificationTypeId;

    private String notificationContent;
}
