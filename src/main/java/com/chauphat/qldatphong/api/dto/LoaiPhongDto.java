package com.chauphat.qldatphong.api.dto;

import java.math.BigDecimal;

public record LoaiPhongDto(
        Integer maLoaiPhong,
        String tenLoaiPhong,
        Integer sucChua,
        BigDecimal giaCoBan,
        String moTa
) {
}
