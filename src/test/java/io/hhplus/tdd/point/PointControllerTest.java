package io.hhplus.tdd.point;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.ApiControllerAdvice;
import io.hhplus.tdd.point.usecase.GetUserPointUseCase;
import io.hhplus.tdd.point.usecase.ListPointHistoriesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PointControllerTest {

    GetUserPointUseCase userPointUseCase = mock(GetUserPointUseCase.class);
    ListPointHistoriesUseCase listHistoriesUseCase = mock(ListPointHistoriesUseCase.class);

    PointController controller = new PointController(userPointUseCase,listHistoriesUseCase);
    MockMvc mockMvc;

    /** 여기서는 Controller 의 단위테스트만 진행하기 위해 standaloneSetup 으로 controller 를 주입해 mockMvc 를 초기화 */
    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApiControllerAdvice())
                .build();
    }


    /**
     * 사용자 포인트 조회 요청 - 성공
     * 올바른 UserId 로 특정 사용자 포인트 조회 요청 시 성공 케이스를 검증합니다.
     *
     * 검증
     * - 응답의 userId는 요청된 userId와 같아야합니다.
     * - 응답의 point 는 usecase 에서 반환받은 point 와 같아야합니다.
     */
    @DisplayName("유효한 사용자 ID로 포인트 조회 요청 시 200 응답과 UserPoint 를 반환한다.")
    @Test
    void shouldReturn200Ok_whenGetPointWithValidUserId() throws Exception {
        //given
        long userId = 1L;
        long point = 2L;
        long updateMillis = System.currentTimeMillis();

        UserPoint userPoint = new UserPoint(userId, point, updateMillis);
        given(userPointUseCase.handle(new UserId(userId))).willReturn(userPoint);

        //when & then
        mockMvc.perform(get("/point/{id}",userId))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(point))
                .andExpect(jsonPath("$.updateMillis").value(updateMillis))
                .andExpect(status().isOk());

    }

    /**
     * 사용자 포인트 조회 - 실패
     * 사용자 포인트 조회 시 UserId 가 음수 또는 0일 경우 정책 위반 예외(무효한 사용자)가 발생 케이스를 검증합니다.
     * 정책 위반 예외 발생시 {@link ApiControllerAdvice#handle(DomainException)} 에서 처리하며, 에러응답을 클라이언트에게 반환합니다.
     *
     * 검증
     * 사용자 ID가 유효하지 않을 경우 다음을 검증합니다.
     * - 에러 코드는 USR-400-INVALID_USER 를 반환해야합니다.
     * - 응답 상태는 400 응답을 반환해야합니다.
     * - 에러 메시지는 비어있지 않아야합니다.
     * @see UserId
     * @throws PolicyViolationException - 정책 위반 예외
     */
    @DisplayName("사용자 ID가 유효하지 않은 경우, 포인트 조회시 400 응답과 에러응답을 반환한다.")
    @Test
    void shouldReturn400BadRequest_whenGetPointWithInvalidUserId() throws Exception {
        //given
        Long userId = -1L;

        //when & then
        mockMvc.perform(get("/point/{id}",userId))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("USR-400-INVALID_USER"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }


    /**
     * 사용자 포인트 내역 조회 - 성공
     * 유효한 사용자 ID로 포인트 내역 조회시 포인트내역 조회 로직을 검증합니다.
     *
     * 검증
     * - 응답 상태는 성공(200) 을 반환해야합니다.
     * - 유스케이스가 반환하는 포인트 내역이, 실제 응답 데이터와 일치해야합니다.
     *
     */
    @DisplayName("유효한 사용자 ID로 포인트 내역 조회시 요청시 200응답과 포인트 내역을 반환한다.")
    @Test
    void shouldReturn200Ok_whenGetPointHistoriesWithValidUserId() throws Exception {
        //given
        long userId = 1L;
        long historyId = 1L;
        long amount = 10L;

        TransactionType type = TransactionType.CHARGE;
        PointHistory pointHistory = new PointHistory(historyId, userId, amount, type , System.currentTimeMillis());
        List<PointHistory> pointHistories = List.of(pointHistory);
        given(listHistoriesUseCase.handle(new UserId(userId))).willReturn(pointHistories);

        //when & then
        MvcResult mvcResult = mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<PointHistory> actual = new ObjectMapper().readValue(json, new TypeReference<>() {});

        assertThat(actual).usingRecursiveComparison().isEqualTo(pointHistories);

    }

    /**
     * 사용자 포인트 내역 조회 - 실패
     * 사용자 ID 로 포인트 내역 조회 시 포인트 내역이 없는 경우 예외 처리를 검증합니다.
     * 포인트 내역이 없는경우 @{link} 이 발생합니다.
     * 리턴 값으론 ErrorResponse를 반환합니다.
     * 검증
     * - 예외 응답의 상태는 NotFound 여야합니다.
     * - 예외 응답의 응답 상태값은 실패코드(404)를 반환해야합니다.
     * - 예외 코드 및 메시지는 반드시 존재해야합니다. (변경 가능성이 있으므로 값이 비어있는지 여부만 확인합니다.)
     *
     *
     */
    @Test
    void shouldReturn404NotFound_whenGetPointHistoriesWithInvalidUserId() throws Exception {
        //given
        long userId = 1L;

        willThrow(new NotFoundException(PointErrorCode.NOT_FOUND, ""))
                .given(listHistoriesUseCase).handle(new UserId(userId));

        //when & then
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

}