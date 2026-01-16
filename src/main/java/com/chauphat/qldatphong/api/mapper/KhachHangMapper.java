package com.chauphat.qldatphong.api.mapper;

import com.chauphat.qldatphong.api.dto.KhachHangDto;
import com.chauphat.qldatphong.domain.entity.KhachHang;
import org.mapstruct.Mapper;

@Mapper
public interface KhachHangMapper {
    KhachHangDto toDto(KhachHang entity);

    KhachHang toEntity(KhachHangDto dto);
}
