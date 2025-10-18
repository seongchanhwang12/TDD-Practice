package io.hhplus.tdd.point.usecase;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.NotFoundException;
import io.hhplus.tdd.point.PointErrorCode;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUserPointHistoriesUseCase {

    private final PointHistoryTable pointHistoryRepo;

    public List<PointHistory> handle(UserId userId) {
        List<PointHistory> histories = pointHistoryRepo.selectAllByUserId(userId.id());
        if(histories.isEmpty()) throw new NotFoundException(PointErrorCode.NOT_FOUND, "user do not have point history");

        return histories;
    }
}
