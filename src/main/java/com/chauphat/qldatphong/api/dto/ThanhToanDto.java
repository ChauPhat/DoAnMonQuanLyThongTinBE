package com.chauphat.qldatphong.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ThanhToanDto(
        Integer maThanhToan,
        Integer maDatPhong,
        LocalDateTime ngayThanhToan,
        BigDecimal soTien,
        String phuongThuc,
        String trangThai
) {
}
