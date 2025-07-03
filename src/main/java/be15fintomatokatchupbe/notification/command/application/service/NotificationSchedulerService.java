package be15fintomatokatchupbe.notification.command.application.service;

import be15fintomatokatchupbe.calendar.command.domain.aggregate.Schedule;
import be15fintomatokatchupbe.calendar.command.mapper.TodayScheduleCommandMapper;
import be15fintomatokatchupbe.notification.command.application.dto.response.NotificationPipeLineResponse;
import be15fintomatokatchupbe.notification.command.domain.aggregate.Notification;
import be15fintomatokatchupbe.notification.command.domain.repository.NotificationRepository;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSchedulerService {

    private final TodayScheduleCommandMapper todayMapper;
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private List<Schedule> getTodaySchedules() {
        return todayMapper.todaySchedule();
    }

    private List<NotificationPipeLineResponse> getPipeLineList() {
        return todayMapper.todayPipeLine();
    }

    @Transactional
    public void sendTodayScheduleNotifications() {
        for (Schedule schedules : getTodaySchedules()) {
            Long userId = schedules.getUserId();
            String notificationContent = schedules.getContent() + "가 예정되어있습니다.";

            Notification notification = Notification.builder()
                    .userId(userId)
                    .notificationTypeId(4L)
                    .getTime(LocalDateTime.now())
                    .notificationContent(notificationContent)
                    .build();

            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void sendWebPushNotification() {
        for (Schedule schedules : getTodaySchedules()) {
            Long userId = schedules.getUserId();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String startTime = formatter.format(schedules.getStartTime());

            String content = "당일 " + schedules.getContent() + "이(가) " + startTime + "에 있습니다.";

            User user = userRepository.findByUserId(userId);
            String token = user.getFcmToken();
            if (token != null && !token.isBlank()) {
                fcmService.sendMessage(token, "오늘의 일정", content);
            }
        }
    }

    @Transactional
    public void sendTodayPipeLineNotifications() {
        for (NotificationPipeLineResponse pipeline : getPipeLineList()) {
            Long userId = pipeline.getUserId();

            String notificationContent = "파이프라인 " + pipeline.getName() + " 발표일입니다.";

            Notification notification = Notification.builder()
                    .userId(userId)
                    .notificationTypeId(4L)
                    .getTime(LocalDateTime.now())
                    .notificationContent(notificationContent)
                    .build();

            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void sendWebPushPipeLineNotification() {
        for (NotificationPipeLineResponse pipeline : getPipeLineList()) {
            Long userId = pipeline.getUserId();

            String content = "당일 " + pipeline.getName() + "이(가) 발표 예정일입니다. ";

            User user = userRepository.findByUserId(userId);
            String token = user.getFcmToken();
            if (token != null && !token.isBlank()) {
                fcmService.sendMessage(token, "오늘의 일정", content);
            }
        }
    }
}
