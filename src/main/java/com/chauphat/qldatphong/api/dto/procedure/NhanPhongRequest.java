package com.chauphat.qldatphong.api.dto.procedure;

import jakarta.validation.constraints.NotNull;

public record NhanPhongRequest(
        @NotNull Integer maDatPhong
) {
}
