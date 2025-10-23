package io.hhplus.tdd.point;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus status();
    String code();
    String messageKey();



}
