package be15fintomatokatchupbe.notification.command.domain.repository;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.notification.command.domain.aggregate.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserIdAndIsDeleted(Long userId, StatusType isDeleted);

    long countByUserIdAndIsReadAndIsDeleted(Long userId, StatusType isRead, StatusType isDeleted);

}