package be15fintomatokatchupbe.notification.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum NotificationErrorCode implements ErrorCode {
    // 알림 오류
    /* 각 도메인마다 ERROR CODE 작성 */
    NOT_FOUND_SATISFACTION("90001", "존재하지 않는 만족도 결과입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_NOTIFICATION("90002", "존재하지 않는 알림입니다.", HttpStatus.BAD_REQUEST ),
    INVALID_USER("90003", "해당 알림을 수신하지 않았습니다.", HttpStatus.FORBIDDEN),
    DELETED_NOTIFICATION("90004", "이미 삭제된 알림입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}