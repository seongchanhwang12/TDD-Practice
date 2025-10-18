package io.hhplus.tdd.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 도메인 예외
 * 전체 도메인에서 던져지는 예외를 정의하는 최상위 예외입니다.
 * ErrorCode - 추상화된 에러코드 정의 인터페이스
 */
@Getter
@RequiredArgsConstructor
public class DomainException extends RuntimeException{
    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
