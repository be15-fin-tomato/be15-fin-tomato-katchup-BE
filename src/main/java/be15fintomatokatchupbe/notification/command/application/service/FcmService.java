package be15fintomatokatchupbe.notification.command.application.service;

import be15fintomatokatchupbe.notification.command.application.dto.request.FcmTokenRequest;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final UserRepository userRepository;

    public void getFcmToken(Long userId, FcmTokenRequest fcmTokenRequest) {
        User user = userRepository.findByUserId(userId);

        user.setFcmToken(fcmTokenRequest.getFcmToken());

        userRepository.save(user);
    }

    /* FireBase로 전송 */
    public void sendMessage(String fcmToken, String title, String body) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM 전송 성공 : {}",  response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 전송 실패 : {}" , e.getMessage());
        }
    }
}
