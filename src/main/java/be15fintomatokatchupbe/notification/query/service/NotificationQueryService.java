package be15fintomatokatchupbe.notification.query.service;

import be15fintomatokatchupbe.notification.query.dto.NotificationsAllDTO;
import be15fintomatokatchupbe.notification.query.dto.response.NotificationsAllResponse;
import be15fintomatokatchupbe.notification.query.mapper.NotificationQueryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationQueryService {

    private final NotificationQueryMapper notificationQueryMapper;

    public NotificationsAllResponse getNotificationsAll(Long userId) {
        List<NotificationsAllDTO> notificationsAll = notificationQueryMapper.getNotificationsAll(userId);
        return NotificationsAllResponse.builder()
                .notifications(notificationsAll)
                .build();
    }
}