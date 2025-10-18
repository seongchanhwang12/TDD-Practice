package io.hhplus.tdd.point;

import lombok.Getter;

@Getter
public class PolicyViolationException extends RuntimeException {
    private final ErrorCode errorCode;

    public PolicyViolationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
