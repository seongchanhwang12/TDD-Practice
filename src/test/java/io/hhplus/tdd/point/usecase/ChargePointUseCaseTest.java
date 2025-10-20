package io.hhplus.tdd.point.usecase;


import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChargePointUseCaseTest {

    UserPointTable userPointRepo;
    PointHistoryTable pointHistoryRepo;
    ChargePointUseCase sut;

    @BeforeEach
    void init(){
        userPointRepo = new UserPointTable();
        pointHistoryRepo = new PointHistoryTable();
        sut = new ChargePointUseCase(userPointRepo,pointHistoryRepo);
    }

    /**
     * 사용자 포인트 충전시, 포인트 누적 검증 - 성공
     *
     * 검증
     * - 사용자 포인트 충전시 신규 충전된 포인트는 기존 저장된 포인트에 누적되어야합니다.
     * - 조회된 사용자와, 포인트를 수정하는 사용자가 같아얍합니다.
     */
    @DisplayName("Given 사용자 ID로, When 이미 저장된 포인트가 있을때 포인트를 저장하면 Then 포인트가 누적된다.")
    @Test
    void givenValidUserId_whenChargePoint_saveUserPoint_AndPointHistoryByUserId() {
        //given
        long userId = 1L;
        long amount = 10L;

        //when
        UserPoint originUserPoint = userPointRepo.insertOrUpdate(userId, 5L);
        UserPoint actual = sut.handle(new UserId(originUserPoint.id()), amount);

        //then
        assertThat(actual.id()).isEqualTo(userId);
        assertThat(actual.point()).isEqualTo(amount + originUserPoint.point());

    }

    /**
     * 사용자 포인트 충전시, 사용자 포인트 충전 내역 추가 - 성공
     *
     * 검증
     * - 포인트 충전하는 사용자와 저장된는 사용자가 같아야합니다.
     * - 사용자 포인 충전시 사용자의 포인트 충전 내역이 저장소에 저장되어야합니다.
     */
    @DisplayName("Given 유효한 사용자 ID로, When 사용자 포인트 충전시, Then 사용자 포인트와 AND 포인트 충전 내역이 저장된다")
    @Test
    void givenValidUserId_whenChargePoint_saveUserPoint_AndChargeHistory() {

        //given
        long userId = 1L;
        long amount = 10L;

        //when
        UserPoint actual = sut.handle(new UserId(userId), amount);

        UserPoint selectedPoint = userPointRepo.selectById(userId);
        List<PointHistory> actualHistories = pointHistoryRepo.selectAllByUserId(userId);

        //then
        assertThat(actual.id()).isEqualTo(userId);
        assertThat(actual.point()).isEqualTo(selectedPoint.point());
        assertThat(actualHistories.get(0).userId()).isEqualTo(userId);
        assertThat(actualHistories.get(0).type()).isEqualTo(TransactionType.CHARGE);

    }

    /**
     * 사용자 포인트 충전시 유효하지 않은 포인트 검증 - 실패
     *
     * 검증
     * - 충전 포인트가 음수일경우 PolicyViolationException 예외가 던져져야합니다.
     * - PolicyViolationException 의 errorCode 는 INVALID_AMOUNT 여야합니다.
     */
    @Test
    void givenInvalidPoint_whenChargePoint_thenThrowPolicyViolationException() {

        //given
        long userId = 1L;
        long amount = -1L;

        //when & then
        assertThatThrownBy(()-> sut.handle(new UserId(userId), amount))
                .isInstanceOf(PolicyViolationException.class)
                .extracting("errorCode")
                .isEqualTo(PointErrorCode.INVALID_AMOUNT);

    }



    

}