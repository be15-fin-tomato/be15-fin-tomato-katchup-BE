package be15fintomatokatchupbe.notifycation.exception;

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
    NOT_FOUND_SATISFACTION("90001", "존재하지 않는 만족도 결과입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}