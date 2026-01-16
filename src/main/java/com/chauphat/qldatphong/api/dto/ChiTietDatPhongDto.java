package com.chauphat.qldatphong.api.dto;

import java.math.BigDecimal;

public record ChiTietDatPhongDto(
        Integer maCtdp,
        Integer maDatPhong,
        Integer maPhong,
        BigDecimal donGia,
        Integer soNgay,
        BigDecimal thanhTien
) {
}
