package com.chauphat.qldatphong.api.dto;

public record PhongDto(
        Integer maPhong,
        Integer maLoaiPhong,
        String tenPhong,
        Integer tang,
        String trangThai
) {
}
