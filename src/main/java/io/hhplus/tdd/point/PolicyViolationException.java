package io.hhplus.tdd.point;

import lombok.Getter;

/**
 * 정책 위반 예외
 * 해당 도메인의 정책 위반시 사용되는 예외입니다.
 */
@Getter
public class PolicyViolationException extends DomainException {

    public PolicyViolationException(ErrorCode errorCode, String message) {
        super(errorCode,message);
    }

}
