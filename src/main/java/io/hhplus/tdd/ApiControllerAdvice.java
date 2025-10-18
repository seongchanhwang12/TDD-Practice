package io.hhplus.tdd;

import io.hhplus.tdd.point.ErrorCode;
import io.hhplus.tdd.point.PolicyViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    private final static Map<String,String> messageStore = Map.of("error.user.invalid", "유효하지 않은 사용자입니다. 동일한 문제가 반복되면 관리자에게 문의해주세요.");

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500", "에러가 발생했습니다."));
    }

    @ExceptionHandler(value = PolicyViolationException.class)
    public ResponseEntity<ErrorResponse> handle(PolicyViolationException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.status())
                .body(new ErrorResponse(
                        errorCode.status().value(),
                        errorCode.code(),
                        messageStore.getOrDefault(errorCode.messageKey(), "처리중 오류가 발생했습니다. 관리자에게 문의해주세요.")));
    }
}
