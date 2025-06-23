package be15fintomatokatchupbe.chat.exception;

import be15fintomatokatchupbe.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ChatErrorCode implements ErrorCode {

    INVALID_CHAT_ROOM_REQUEST("70001", "채팅 참여자는 한 명 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    SINGLE_PARTICIPANT_NOT_ALLOWED("70002", "자기 자신만으로는 채팅방을 만들 수 없습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("70003", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_CHAT_ROOM("70004", "이미 동일한 채팅방이 존재합니다.", HttpStatus.CONFLICT),
    UNAUTHORIZED_CHAT_ACCESS("70005", "채팅 기능은 로그인 후 이용 가능합니다.", HttpStatus.UNAUTHORIZED),
    CHAT_ROOM_NOT_FOUND("70006", "채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_EXITED_CHAT("70007","이미 나간 상태입니다." , HttpStatus.CONFLICT );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
