package be15fintomatokatchupbe.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}