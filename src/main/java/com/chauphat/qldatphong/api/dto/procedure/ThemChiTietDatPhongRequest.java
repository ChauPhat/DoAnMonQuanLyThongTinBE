package com.chauphat.qldatphong.api.dto.procedure;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ThemChiTietDatPhongRequest(
        @NotNull Integer maDatPhong,
        @NotNull Integer maPhong,
        @NotNull @Positive BigDecimal donGia
) {
}
