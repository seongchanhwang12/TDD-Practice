package io.hhplus.tdd.point.usecase;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class GetUserPointUseCaseTest {

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
        UserPoint result = sut.handle(userId);

        //then
        then(mockRepo).should().selectById(userId);
        assertThat(result.id()).isEqualTo(userPoint.id());
        assertThat(result.point()).isEqualTo(userPoint.point());

    }

    @Test
    @DisplayName("Given 유효하지 않은 userId, When 포인트를 조회하면 Then 0포인트를 반환한다.")
    void givenInvalidUserId_whenGetUserPoint_thenReturnEmptyUserPoint() {
        // given
        GetUserPointUseCase sut = new GetUserPointUseCase(new UserPointTable());

        long userId = 0L;

        // when
        UserPoint result = sut.handle(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);

    }
}