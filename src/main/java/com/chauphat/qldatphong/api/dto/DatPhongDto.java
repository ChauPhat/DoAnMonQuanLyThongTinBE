package com.chauphat.qldatphong.api.dto;

import java.time.LocalDateTime;

public record DatPhongDto(
        Integer maDatPhong,
        Integer maKh,
        Integer maNv,
        LocalDateTime ngayDat,
        LocalDateTime ngayNhan,
        LocalDateTime ngayTra,
        String trangThai
) {
}
