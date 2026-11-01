package HaruIt.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "C001", "입력값이 올바르지 않습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "C002", "요청 본문을 읽을 수 없습니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "C003", "요청 값의 타입이 올바르지 않습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "C004", "필수 요청 파라미터가 누락되었습니다."),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "존재하지 않는 API입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C006", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C007", "지원하지 않는 HTTP 메서드입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "C008", "지원하지 않는 Content-Type입니다."),
    CLIENT_ERROR(HttpStatus.BAD_REQUEST, "C099", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C999", "서버 내부 오류가 발생했습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "U002", "현재 비밀번호가 일치하지 않습니다."),
    SAME_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "기존 비밀번호와 같습니다."),
    INVALID_SLEEP_TIME(HttpStatus.BAD_REQUEST, "U004", "취침 시각과 기상 시각이 같을 수 없습니다."),
    INVALID_ALARM_MINUTES(HttpStatus.BAD_REQUEST, "U005", "알림 시간은 5~120분 사이여야 합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
