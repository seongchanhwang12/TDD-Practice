package io.hhplus.tdd.point;

import jakarta.validation.constraints.Positive;

public record UsePointRequest(@Positive long point) {
}
