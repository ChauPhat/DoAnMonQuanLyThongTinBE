package com.chauphat.qldatphong.api.dto.procedure;

import jakarta.validation.constraints.NotNull;

public record TraPhongRequest(
        @NotNull Integer maDatPhong
) {
}
