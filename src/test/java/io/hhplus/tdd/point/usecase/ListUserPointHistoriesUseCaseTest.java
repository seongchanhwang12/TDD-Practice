package io.hhplus.tdd.point.usecase;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class ListUserPointHistoriesUseCaseTest {

    PointHistoryTable pointHistoryRepo = mock(PointHistoryTable.class);
    ListUserPointHistoriesUseCase sut = new ListUserPointHistoriesUseCase(pointHistoryRepo);


    /**
     * 특정 사용자의 포인트 내역 조회 시 성공 케이스 테스트
     * 조회한 사용자의 포인트 내역이 정상적으로 반횐 되는지 검증합니다.
     *
     * - 포인트 저장소에서 사용자 ID로 데이터 조회가 1회 실행되어야합니다.
     * - 조회된 포인트 내역의 사용자 정보가 조회된 포인트 내역의 사용자 정보와 같아야합니다.
     */
    @DisplayName("유효한 사용자 ID가 있고, 포인트내역 조회시, 사용자의 포인트내역을 반환한다")
    @Test
    void givenValidUserIdWhenListPointHistoriesThenReturnListHistories() {
        //given
        long userId = 1L;
        long historyId = 1L;
        long amount = 100L;
        TransactionType type = TransactionType.CHARGE;
        long updateMillis = System.currentTimeMillis();
        PointHistory pointHistory = new PointHistory(historyId,userId, amount, type,updateMillis);
        List<PointHistory> stubHistories = List.of(pointHistory);

        //when
        given(pointHistoryRepo.selectAllByUserId(userId)).willReturn(stubHistories);
        List<PointHistory> histories = sut.handle(new UserId(userId));

        //then
        then(pointHistoryRepo).should().selectAllByUserId(userId);
        assertThat(histories).isNotEmpty();
        assertThat(histories.size()).isEqualTo(1);
        assertThat(histories.get(0).id()).isEqualTo(historyId);
        assertThat(histories.get(0).amount()).isEqualTo(amount);
        assertThat(histories.get(0).type()).isEqualTo(type);
        assertThat(histories.get(0).updateMillis()).isEqualTo(updateMillis);
        
    }

    /**
     * 특정 사용자의 포인트 내역 조회시 포인트 내역이 없을경우 실패 케이스
     *
     * - 특정 사용자의 포인트 내역이 없을경우 NotFoundException 을 던져져야합니다.
     * - NotFoundException 의 ErrorCode 는 PointErrorCode.NotFound 를 담고있어야 합니다.
     */
    @Test
    void givenInvalidUserIdWhenListPointHistoriesThenReturnEmptyList() {
        //given
        long userId = 1L;

        //when & then
        assertThatThrownBy(() -> sut.handle(new UserId(userId)))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(PointErrorCode.NOT_FOUND);

        
    }
}