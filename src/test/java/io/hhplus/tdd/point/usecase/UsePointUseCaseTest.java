package io.hhplus.tdd.point.usecase;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointErrorCode;
import io.hhplus.tdd.point.PolicyViolationException;
import io.hhplus.tdd.point.UserId;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class UsePointUseCaseTest {

    UserPointTable userPointTable = new UserPointTable() ;
    UsePointUseCase sut = new UsePointUseCase(userPointTable);

    /**
     * 특정 사용자의 포인트 사용 테스트 - 성공
     * 사용자가 포인트 사용시 가지고 있던 포인트가 차감되고, 잔액을 반환합니다.
     *
     * 검증
     * - 사용한 포인트 만큼 기존 포인트에서 차감되어야합니다.
     * - 포인트 사용자의 ID가 포인트 잔액의 사용자 ID와 동일해야합니다.
     */
    @DisplayName("특정 사용자가 특정야의 포인트를 사용시, 사용양 만틈 포인트가 차감된다. ")
    @Test
    void givenValidIdAndAmount_whenUsePoint_thenSubtracted() {
        //given
        long userId = 1L, originPoint = 100L, usedPoint = 70L;

        //when
        userPointTable.insertOrUpdate(userId, originPoint);
        UserPoint actual = sut.handle(new UserId(userId), usedPoint);

        //then
        assertThat(actual.point()).isEqualTo(originPoint-usedPoint);
        assertThat(actual.id()).isEqualTo(userId);

    }

    /**
     * 특정 사용자의 포인트 사용 테스트 - 실패
     * 사용자가 포인트 사용시 가지고 있던 포인트가 부족하면, 정책예외에 포인트 부족 예외 코드를 담아 던집니다.
     *
     * 검증
     * - 포인트는 부족시, 정책 위반 예외가 던져집니다.
     * - 정책위반 예외는 포인트 부족 에러 코드를 포함합니다.
     */
    @Test
    void givenInValidIdAndAmount_whenUsePoint_thenThrow_PolicyViolationException () {

        //given
        long userId = 1L,  usedPoint = 70L;

        //when
        assertThatThrownBy(()->sut.handle(new UserId(userId), usedPoint))
                .isInstanceOf(PolicyViolationException.class)
                .extracting("errorCode")
                .isEqualTo(PointErrorCode.INSUFFICIENT_POINT);
    }

}