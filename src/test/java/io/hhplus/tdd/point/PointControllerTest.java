package io.hhplus.tdd.point;

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
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    /**
     * 사용자 포인트 조회 요청 - 성공
     * @throws Exception
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


}