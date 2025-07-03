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
    INSTAGRAM_STATS_ERROR("50002", "인스타그램 통계 조회 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    MEDIA_NOT_FOUND("50003", "게시물 조회에 실패 실패했습니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_MEDIA_TYPE("50004", "지원하지 않는 게시글 형식입니다.", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND("50005", "토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    FAILED_TOKEN_EXCHANGE("50006", "액세스 토큰 교환에 실패했습니다.", HttpStatus.BAD_REQUEST),
    LONG_LIVED_TOKEN_EXCHANGE_FAILED("50007", "장기 액세스 토큰 연장에 실패했습니다.", HttpStatus.BAD_REQUEST),
    LONG_LIVED_TOKEN_REFRESH_FAILED("50007", "장기 액세스 토큰 재발급에 실패했습니다.", HttpStatus.BAD_REQUEST),
    INSTAGRAM_ACCOUNT_INFO_ERROR("50008", "인스타그램 계정 정보를 불러올 수 없습니다.", HttpStatus.BAD_REQUEST),
    YOUTUBE_COMMENT_FETCH_FAILED("50009", "유튜브 댓글 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
