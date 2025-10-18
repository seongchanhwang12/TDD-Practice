package io.hhplus.tdd.point;

import io.hhplus.tdd.point.usecase.GetUserPointUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private final GetUserPointUseCase getUserPointUseCase;

    /**
     * 특정 사용자의 포인트 조회 요청 처리
     * TODO - API 스펙 변경시 대응을 위해 추후 공통 응답 객체를 담아 리턴하는 방식을 고려중입니다.
     * @return UserPoint - 사용자 포인트 정보
     */
    @GetMapping("{id}")
    public UserPoint point(@PathVariable long id) {
        return getUserPointUseCase.handle(new UserId(id));
    }

    /**
     *
     */
    @GetMapping("{id}/histories")
    public List<PointHistory> history(
            @PathVariable long id
    ) {
        return List.of();
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    public UserPoint charge(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        return new UserPoint(0, 0, 0);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    public UserPoint use(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        return new UserPoint(0, 0, 0);
    }
}
