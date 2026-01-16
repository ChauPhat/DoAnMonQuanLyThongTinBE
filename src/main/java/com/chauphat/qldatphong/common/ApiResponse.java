package com.chauphat.qldatphong.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        Instant timestamp,
        T data,
        ApiError error
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(Instant.now(), data, null);
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        return new ApiResponse<>(Instant.now(), null, error);
    }
}
