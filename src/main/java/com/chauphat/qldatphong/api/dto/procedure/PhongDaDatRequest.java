package com.chauphat.qldatphong.api.dto.procedure;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PhongDaDatRequest(
        @NotNull LocalDateTime tuNgay,
        @NotNull LocalDateTime denNgay
) {
}
