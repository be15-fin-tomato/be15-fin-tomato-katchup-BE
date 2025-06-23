package be15fintomatokatchupbe.client.command.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ClientErrorCode implements ErrorCode {

    INVALID_STATUS("30001", "존재하지 않는 고객사 상태입니다.", HttpStatus.BAD_REQUEST),
    INVALID_MANAGER_STATUS("30002", "존재하지 않는 고객사 사원 상태입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
