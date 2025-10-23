package io.hhplus.tdd;

public record ErrorResponse(
        int status,
        String code,
        String message
) {
}
