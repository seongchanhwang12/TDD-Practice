package io.hhplus.tdd.point.usecase;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserId;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsePointUseCase {

    private final UserPointTable userPointTable;

    public UserPoint handle(UserId userId, long usingPoint) {
        UserPoint userPoint = userPointTable.selectById(userId.id());
        UserPoint usedPoint = userPoint.minus(usingPoint);
        return userPointTable.insertOrUpdate(usedPoint.id(), usedPoint.point());
    }
}
