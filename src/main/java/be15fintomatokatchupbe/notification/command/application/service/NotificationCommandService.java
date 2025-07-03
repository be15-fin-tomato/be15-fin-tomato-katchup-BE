package be15fintomatokatchupbe.notification.command.application.service;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.notification.command.domain.aggregate.Notification;
import be15fintomatokatchupbe.notification.command.domain.repository.NotificationRepository;
import be15fintomatokatchupbe.notification.exception.NotificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {

    private final NotificationRepository notificationRepository;

//    public void deleteAllNotification(Long userId) {
//        List<Notification> notifications = notificationRepository.findAllByUserIdAndIsDeleted(userId, StatusType.N);
//
//    }

    public void deleteNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(NotificationErrorCode.NOT_FOUND_NOTIFICATION));

        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException(NotificationErrorCode.INVALID_USER);
        }

        if (notification.getIsDeleted() == StatusType.Y) {
            throw new BusinessException(NotificationErrorCode.DELETED_NOTIFICATION);
        }
        
        notification.softdelete();
        notificationRepository.save(notification);

    }
}