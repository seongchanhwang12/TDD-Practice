package io.hhplus.tdd.point;


/**
 * NotFoundException 예외
 * 클라이언트가 요청한 자원을 조회할 수 없는 경우 사용합니다.
 */
public class NotFoundException extends DomainException {

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
