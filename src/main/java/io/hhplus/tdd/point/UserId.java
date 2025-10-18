package io.hhplus.tdd.point;

import com.sun.jdi.InternalException;

/**
 * UserId VO
 * 사용자 ID 대한 불변식을 보장하며, 0 이하이거나 null 인 ID는 생성할 수 없습니다.
 * @param id - 사용자 고유 식별자
 */
public record UserId(Long id) {
    public UserId {
        if( id == null || id <= 0 ){
            throw new PolicyViolationException(UserErrorCode.INVALID_USER, "UserId is not valid");
        }
    }

    public Long value() {
        return id;
    }
}
