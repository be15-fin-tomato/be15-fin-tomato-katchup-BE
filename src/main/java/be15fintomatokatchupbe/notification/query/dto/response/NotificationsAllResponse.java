package be15fintomatokatchupbe.notification.query.dto.response;

import be15fintomatokatchupbe.notification.query.dto.NotificationsAllDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NotificationsAllResponse {

    private final List<NotificationsAllDTO> notifications;

}