package be15fintomatokatchupbe.contract.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum DigitalContractErrorCode implements ErrorCode {

    NOT_FOUND("30001", "전자 계약서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_PERMISSION("30002", "전자 계약서를 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    DUPLICATE_TEMPLATE("30003", "이미 존재하는 템플릿 이름입니다.", HttpStatus.CONFLICT),
    INVALID_TEMPLATE_NAME("30004", "유효하지 않은 템플릿 이름입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_CONTENT("30005", "내용이 비어 있습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
