package com.chauphat.qldatphong.api.dto.procedure;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DoanhThuTheoThangRequest(
        @NotNull @Min(1) @Max(12) Integer thang,
        @NotNull @Min(2000) @Max(2100) Integer nam
) {
}
