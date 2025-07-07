package be15fintomatokatchupbe.notification.command.application.controller;

import be15fintomatokatchupbe.notification.command.application.service.NotificationSchedulerService;
import be15fintomatokatchupbe.notification.command.domain.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationSchedulerService notificationSchedulerService;
    private final SseEmitterRepository sseEmitterRepository;

    /* 새벽에 캘린더 당일 알림 db에 넣어놓기 */
    @Scheduled(cron = "0 1 3 * * *") // 매일 새벽 3시 1분에 실행
//    @Scheduled(cron = "*/30 * * * * *") // 테스트용 매 59초마다 실행
    public void sendTodayScheduleNotification() {
        log.info("실행 시작");
        notificationSchedulerService.sendTodayScheduleNotifications();
    }

    /* 오전 8시30분에 캘린더 당일 웹푸시로 알림 보내주기 */
    @Scheduled(cron = "0 30 8 * * *")
//    @Scheduled(cron = "*/59 * * * * *") // 테스트용 매 59초마다 실행
    public void sendWebPushNotification() {
        notificationSchedulerService.sendWebPushNotification();
    }

    /* 새벽에 파이프라인 발표일 당일 알림 db에 넣어놓기 */
    @Scheduled(cron = "0 2 3 * * *") // 매일 새벽 3시 2분에 실행
//    @Scheduled(cron = "*/59 * * * * *") // 테스트용 매 59초마다 실행
    public void sendTodayPipeLineNotification() {
        notificationSchedulerService.sendTodayPipeLineNotifications();
    }

    /* 오전 8시35분에 파이프라인 발표일 당일 웹푸시로 알림 보내주기 */
    @Scheduled(cron = "15 30 8 * * *") // 매일 8시 30분 15초에 실행
//    @Scheduled(cron = "*/59 * * * * *") // 테스트용 매 59초마다 실행
    public void sendWebPushPipeLineNotification() {
        notificationSchedulerService.sendWebPushPipeLineNotification();
    }

    /* 의미없는 데이터를 계속 보내서 sse 연결 유지 */
    @Scheduled(cron = "*/25 * * * * *")
    public void sendHeartbeat() {
        Map<Long, SseEmitter> emitters = sseEmitterRepository.findAll();

        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (IOException e) {
                sseEmitterRepository.delete(userId);
            }
        });
    }
}
