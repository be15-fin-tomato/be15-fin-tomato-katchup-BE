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
    INVALID_MANAGER_STATUS("30002", "존재하지 않는 고객사 사원 상태입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("30003", "해당 고객사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CLIENT_MANAGER_NOT_FOUND("30004", "존재하지 않는 사원입니다.", HttpStatus.NOT_FOUND),
    CLIENT_COMPANY_NOT_FOUND("30005", "존재하지 않는 회사입니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
