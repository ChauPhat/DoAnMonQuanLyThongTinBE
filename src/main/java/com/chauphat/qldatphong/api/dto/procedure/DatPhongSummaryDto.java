package com.chauphat.qldatphong.api.dto.procedure;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DatPhongSummaryDto(
        Integer maDatPhong,
        Integer maKh,
        String hoTenKh,
        Integer maNv,
        String tenNv,
        LocalDateTime ngayDat,
        LocalDateTime ngayNhan,
        LocalDateTime ngayTra,
        String trangThai,
        Integer soPhong,
        BigDecimal tongTien
) {
}
