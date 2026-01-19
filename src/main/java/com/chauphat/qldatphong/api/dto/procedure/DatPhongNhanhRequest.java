package com.chauphat.qldatphong.api.dto.procedure;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record DatPhongNhanhRequest(
        @NotNull Integer maKh,
        @NotNull Integer maNv,
        @NotNull LocalDateTime ngayNhan,
        @NotNull LocalDateTime ngayTra,
        @NotEmpty List<Integer> maPhong
) {
}
