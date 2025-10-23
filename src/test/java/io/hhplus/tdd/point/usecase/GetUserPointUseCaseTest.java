package io.hhplus.tdd.point.usecase;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserId;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


/**
 * 사용자 포인트 조회 케이스 단위 테스트
 * 특정 사용자 적립 포인트 조회 시 성공/실패 케이스를 다룹니다.
 */
class GetUserPointUseCaseTest {


    /**
     * 사용자 포인트 조회 성공 - 정상 요청
     */
    @Test
    @DisplayName("Given 유효한 userId, When 포인트를 조회하면 Then 유저 포인트를 반환한다")
    void givenValidUserId_whenGetUserPoint_thenReturnUserPoint() {
        // given
        UserPointTable mockRepo = mock(UserPointTable.class);
        GetUserPointUseCase sut = new GetUserPointUseCase(mockRepo);

        long userId = 1L;
        long point = 1L;

        UserPoint userPoint = new UserPoint(userId, point, System.currentTimeMillis());
        given(mockRepo.selectById(userId)).willReturn(userPoint);

        //when
        UserPoint result = sut.handle(new UserId(userId));

        //then
        then(mockRepo).should().selectById(userId);
        assertThat(result.id()).isEqualTo(userPoint.id());
        assertThat(result.point()).isEqualTo(userPoint.point());

    }

    /**
     * 사용자 포인트 조회 실패 - 사용자 ID로 적립된 포인트 내역이 존재하지 않는 경우
     */
    @Test
    @DisplayName("Given 유효하지 않은 userId, When 포인트를 조회하면 Then 0포인트를 반환한다.")
    void givenInvalidUserId_whenGetUserPoint_thenReturnEmptyUserPoint() {
        // given
        GetUserPointUseCase sut = new GetUserPointUseCase(new UserPointTable());

        UserId userId = new UserId(1L);

        // when
        UserPoint result = sut.handle(userId);

        // then
        assertThat(result.id()).isEqualTo(userId.id());
        assertThat(result.point()).isZero();

    }

    

}