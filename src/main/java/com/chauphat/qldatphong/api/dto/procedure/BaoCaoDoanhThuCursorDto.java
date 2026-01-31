package com.chauphat.qldatphong.api.dto.procedure;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BaoCaoDoanhThuCursorDto(
        Integer maDatPhong,
        Integer maKh,
        String hoTenKh,
        LocalDateTime ngayDat,
        String trangThai,
        BigDecimal tongTien
) {
}
