package be15fintomatokatchupbe.influencer.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum InfluencerErrorCode implements ErrorCode {

    INFLUENCER_NOT_FOUND("20001", "인플루언서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    YOUTUBE_ACCOUNT_NOT_FOUND("20002", "유튜브 계정이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INSTAGRAM_ACCOUNT_NOT_FOUND("20003", "인스타그램 계정이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    INVALID_INFLUENCER_ID("20004", "유효하지 않은 인플루언서 ID입니다.", HttpStatus.BAD_REQUEST),
    FAILED_TO_FETCH_YOUTUBE_DATA("20005", "유튜브 데이터 조회에 실패했습니다.", HttpStatus.BAD_GATEWAY),
    FAILED_TO_FETCH_INSTAGRAM_DATA("20006", "인스타그램 데이터 조회에 실패했습니다.", HttpStatus.BAD_GATEWAY),

    TAGS_PARSING_FAILED("20007", "태그 데이터 파싱에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INFLUENCER_ALREADY_EXISTS("20008", "이미 존재하는 인플루언서입니다.", HttpStatus.CONFLICT),

    UNAUTHORIZED_ACCESS("2009", "인플루언서 정보에 접근할 권한이 없습니다.", HttpStatus.UNAUTHORIZED)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}

