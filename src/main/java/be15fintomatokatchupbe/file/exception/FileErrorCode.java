package be15fintomatokatchupbe.file.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum FileErrorCode implements ErrorCode {

    FILE_FORMAT_ERROR("12001", "잘못된 파일 형식입니다.", HttpStatus.NOT_FOUND),
    FILE_TOO_BIG("12002","파일 크기는 20MB로 제한 됩니다." ,HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND("12003", "파일을 찾을수 없습니다.", HttpStatus.BAD_REQUEST),
    FILE_DOWNLOAD_ERROR("12004", "파일 다운로드에 실패하였습니다,", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}

