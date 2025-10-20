package io.hhplus.tdd.point;

import jakarta.validation.constraints.Positive;

public record PointChargeRequest(@Positive long point) {

}
