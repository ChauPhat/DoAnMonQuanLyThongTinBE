package com.chauphat.qldatphong.api.dto.procedure;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DatPhongRequest(
        @NotNull Integer maKh,
        @NotNull Integer maNv,
        @NotNull LocalDateTime ngayNhan,
        @NotNull LocalDateTime ngayTra
) {
}
