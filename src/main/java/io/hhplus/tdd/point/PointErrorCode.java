package io.hhplus.tdd.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum PointErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "PNT-404-NOT_FOUND", "error.point.not_found"),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "PNT-400-INVALID_AMOUNT", "error.point.invalid_amount"  );
    private final HttpStatus status;
    private final String code;
    private final String messageKey;
}
