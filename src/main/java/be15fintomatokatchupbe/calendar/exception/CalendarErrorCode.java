package be15fintomatokatchupbe.calendar.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum CalendarErrorCode implements ErrorCode {

    SCHEDULE_NOT_FOUND("60001", "존재하지 않는 일정입니다.", HttpStatus.NOT_FOUND),
    ACCESS_DENIED("60002", "해당 일정에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_TIME("60003", "시작 시간이 종료 시간보다 늦을 수 없습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
