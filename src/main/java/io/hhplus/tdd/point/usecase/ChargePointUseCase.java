package io.hhplus.tdd.point.usecase;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserId;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointUseCase {

        private final UserPointTable userPointRepo;
        private final PointHistoryTable pointHistoryRepo;


        public UserPoint handle(UserId userId, long pointAmount) {
        UserPoint originPoint = userPointRepo.selectById(userId.id());
        UserPoint newPoint = userPointRepo.insertOrUpdate(userId.id(), originPoint.plus(pointAmount).point());
        pointHistoryRepo.insert(userId.id(),newPoint.point(), TransactionType.CHARGE,System.currentTimeMillis());
        return newPoint;

    }
}
