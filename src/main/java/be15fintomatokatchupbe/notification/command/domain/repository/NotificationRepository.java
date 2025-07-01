package be15fintomatokatchupbe.notification.command.domain.repository;

import be15fintomatokatchupbe.notification.command.domain.aggregate.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
