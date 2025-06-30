package be15fintomatokatchupbe.campaign.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum CampaignErrorCode implements ErrorCode {

    /* 4XXXX 에러코드 할당*/
    CAMPAIGN_STATUS_NOT_FOUND("40001", "존재하지 않는 캠페인 상태입니다.", HttpStatus.NOT_FOUND),
    PIPELINE_STEP_NOT_FOUND("40002", "존재하지 않는 파이프라인 단계입니다.", HttpStatus.NOT_FOUND),
    CAMPAIGN_NOT_FOUND("40003", "존재하지 않는 캠페인입니다.", HttpStatus.NOT_FOUND),
    PIPELINE_STATUS_NOT_FOUND("40004", "존재하지 않는 파이프라인 상태입니다.", HttpStatus.NOT_FOUND),
    INVALID_CAMPAIGN_STATUS("40005","캠페인 상태가 정확하지 않습니다." ,HttpStatus.BAD_REQUEST),
    INVALID_YOUTUBE_LINK("40006","존재하지 않는 링크입니다." ,HttpStatus.BAD_REQUEST),
    IDEA_ACCESS_DENIED("40007", "해당 의견에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    DELETED_IDEA("40008", "이미 삭제된 의견입니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER("40009", "귀하가 작성한 의견이 아닙니다.", HttpStatus.FORBIDDEN),
    IDEA_NOT_FOUND("40010", "존재하지 않는 의견입니다.", HttpStatus.NOT_FOUND);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}