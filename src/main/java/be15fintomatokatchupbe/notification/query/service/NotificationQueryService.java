package be15fintomatokatchupbe.notification.query.service;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.notification.command.domain.repository.NotificationRepository;
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
    private final NotificationRepository notificationRepository;

    public NotificationsAllResponse getNotificationsAll(Long userId) {
        List<NotificationsAllDTO> notificationsAll = notificationQueryMapper.getNotificationsAll(userId);
        return NotificationsAllResponse.builder()
                .notifications(notificationsAll)
                .build();
    }

    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countByUserIdAndIsReadAndIsDeleted(
                userId, StatusType.N, StatusType.N
        );
    }
}