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

    CAMPAIGN_STATUS_NOT_FOUND("40001", "캠페인 상태가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
