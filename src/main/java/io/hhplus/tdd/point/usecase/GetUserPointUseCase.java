package io.hhplus.tdd.point.usecase;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserPointUseCase {

    private final UserPointTable userPointTable;

    public UserPoint handle(long userId) {
        return userPointTable.selectById(userId);
    }
}
