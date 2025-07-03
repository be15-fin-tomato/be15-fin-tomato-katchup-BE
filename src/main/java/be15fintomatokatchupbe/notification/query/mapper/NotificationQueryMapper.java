package be15fintomatokatchupbe.notification.query.mapper;

import be15fintomatokatchupbe.notification.query.dto.NotificationsAllDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationQueryMapper {

    List<NotificationsAllDTO> getNotificationsAll(Long userId);

}
