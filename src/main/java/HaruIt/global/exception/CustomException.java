package HaruIt.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus status;
    private final ErrorCode errorCode;
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getStatus();
    }

    public CustomException(ErrorCode errorCode, String detail) {
        super(detail);
        this.errorCode = errorCode;
        this.status = errorCode.getStatus();
    }

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.errorCode = null;
        this.status = status;
    }
}
