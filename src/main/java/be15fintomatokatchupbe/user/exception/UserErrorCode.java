package be15fintomatokatchupbe.user.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum UserErrorCode implements ErrorCode {
    // member 오류
    /* 각 도메인마다 ERROR CODE 작성 */
    DUPLICATE_EMAIL_EXISTS("10001", "이미 가입된 이메일입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_ID_EXISTS("10002", "이미 가입된 사원코드입니다.", HttpStatus.BAD_REQUEST),
    INVALID_LOGIN_ID_REQUEST("10003", "잘못된 사원코드 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_REQUEST("10004", "잘못된 비밀번호 입니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH("10005", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("10006", "이메일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("10007", "회원이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_MISMATCH("10008", "두 비밀번호가 서로 일치하지않습니다.", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}