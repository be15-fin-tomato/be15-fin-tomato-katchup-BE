package be15fintomatokatchupbe.email.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum EmailErrorCode implements ErrorCode {
    // email 오류
    /* 각 도메인마다 ERROR CODE 작성 */
    NOT_FOUND_SATISFACTION("90001", "존재하지않는 만족도 결과입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_REQUESTED_SATISFACTION("90002", "만족도 조사를 요청한 이력이 있습니다.", HttpStatus.BAD_REQUEST);



    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}