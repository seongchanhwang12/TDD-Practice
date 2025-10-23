package io.hhplus.tdd;

import io.hhplus.tdd.point.DomainException;
import io.hhplus.tdd.point.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    private final static Map<String,String> messageSource = Map.of(
            "error.user.invalid", "유효하지 않은 사용자입니다. 동일한 문제가 반복되면 관리자에게 문의해주세요.",
            "error.point.not_found","포인트 적립 내역이 없습니다.",
            "jakarta.validation.constraints.Positive.message","충전할 포인트는 0보다 커야합니다.");

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500", "에러가 발생했습니다."));
    }

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<ErrorResponse> handle(DomainException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.status())
                .body(new ErrorResponse(
                        errorCode.status().value(),
                        errorCode.code(),
                        messageSource.getOrDefault(errorCode.messageKey(), "처리중 오류가 발생했습니다. 관리자에게 문의해주세요.")));
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {


        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : e.getMessage();

        return ResponseEntity.unprocessableEntity().body(
                new ErrorResponse(
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        fieldError != null? fieldError.getCode() : "UNKNOWN",
                        message));
    }


}
