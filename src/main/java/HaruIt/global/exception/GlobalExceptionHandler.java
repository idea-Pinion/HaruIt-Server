package HaruIt.global.exception;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ProblemDetail handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[{}] {}", errorCode.getCode(), e.getMessage());
        return createProblemDetail(errorCode, e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "reason", Objects.requireNonNullElse(fe.getDefaultMessage(), "")))
                .toList();

        log.warn("[{}] validation failed: {}", errorCode.getCode(), errors);

        ProblemDetail pd = createProblemDetail(errorCode, errorCode.getMessage());
        pd.setProperty("errors", errors);
        return ResponseEntity.status(errorCode.getStatus()).body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {
        ResponseEntity<Object> response =
                super.handleExceptionInternal(ex, body, headers, statusCode, request);

        if (response != null && response.getBody() instanceof ProblemDetail pd) {
            ErrorCode errorCode = resolveErrorCode(ex, statusCode);
            pd.setTitle(errorCode.name());
            pd.setDetail(errorCode.getMessage());
            pd.setProperty("code", errorCode.getCode());
            log.warn("[{}] {}: {}", errorCode.getCode(), ex.getClass().getSimpleName(), ex.getMessage());
        }
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception e) {
        log.error("Unhandled exception", e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return createProblemDetail(errorCode, errorCode.getMessage());
    }

    private ErrorCode resolveErrorCode(Exception ex, HttpStatusCode status) {
        return switch (ex) {
            case HttpMessageNotReadableException _ -> ErrorCode.INVALID_REQUEST_BODY;
            case TypeMismatchException _ -> ErrorCode.INVALID_TYPE;
            case MissingServletRequestParameterException _ -> ErrorCode.MISSING_PARAMETER;
            case HandlerMethodValidationException _ -> ErrorCode.INVALID_INPUT;
            case NoResourceFoundException _ -> ErrorCode.API_NOT_FOUND;
            case HttpRequestMethodNotSupportedException _ -> ErrorCode.METHOD_NOT_ALLOWED;
            case HttpMediaTypeNotSupportedException _ -> ErrorCode.UNSUPPORTED_MEDIA_TYPE;
            default -> status.is4xxClientError() ? ErrorCode.CLIENT_ERROR : ErrorCode.INTERNAL_SERVER_ERROR;
        };
    }

    private ProblemDetail createProblemDetail(ErrorCode errorCode, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(errorCode.getStatus(), detail);
        pd.setTitle(errorCode.name());
        pd.setProperty("code", errorCode.getCode());
        return pd;
    }
}
