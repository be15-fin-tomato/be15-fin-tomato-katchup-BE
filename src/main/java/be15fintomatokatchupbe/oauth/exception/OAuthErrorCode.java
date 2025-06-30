package be15fintomatokatchupbe.oauth.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum OAuthErrorCode implements ErrorCode {
    /* OAuth Error */
    INVALID_OAUTH_CODE("50001", "코드 값이 비어있거나 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    INSTAGRAM_STATS_ERROR("50002", "인스타그램 통계 조회 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
