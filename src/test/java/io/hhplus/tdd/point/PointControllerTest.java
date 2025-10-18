package io.hhplus.tdd.point;

import io.hhplus.tdd.ApiControllerAdvice;
import io.hhplus.tdd.point.usecase.GetUserPointUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PointControllerTest {

    GetUserPointUseCase useCase = mock(GetUserPointUseCase.class);
    PointController controller = new PointController(useCase);
    MockMvc mockMvc;

    /** 별도 통합테스트를 구성하므로 여기서는 Controller 의 단위테스트만 진행하기 위해 standaloneSetup 으로 controller 를 주입해 mockMvc 를 초기화 */
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
        given(useCase.handle(new UserId(userId))).willReturn(userPoint);

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
     * 정책 위반 예외 발생시 {@link ApiControllerAdvice#handle(PolicyViolationException)} 에서 처리하며, 에러응답을 클라이언트에게 반환합니다.
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


}