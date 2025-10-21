package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {
    public UserPoint {
        if(point < 0){
            throw new PolicyViolationException(PointErrorCode.INVALID_AMOUNT,"포인트는 0 이하일수 없습니다.");
        }

    }

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }


    public UserPoint plus(long newAmount) {
        return new UserPoint(id, point + newAmount, System.currentTimeMillis());
    }

    public UserPoint minus(long usedPoint) {
        long result = point - usedPoint;
        if(result < 0){
            throw new PolicyViolationException(PointErrorCode.INSUFFICIENT_POINT, "포인트가 부족합니다.");
        }
        return new UserPoint(id, result, System.currentTimeMillis());
    }
}
