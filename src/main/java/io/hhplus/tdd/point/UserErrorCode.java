package io.hhplus.tdd.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    INVALID_USER(HttpStatus.BAD_REQUEST, "USR-400-INVALID_USER", "error.user.invalid_user");
    private final HttpStatus status;
    private final String code;
    private final String messageKey;

}
