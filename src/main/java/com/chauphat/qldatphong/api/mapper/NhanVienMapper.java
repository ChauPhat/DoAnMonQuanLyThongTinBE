package com.chauphat.qldatphong.api.mapper;

import com.chauphat.qldatphong.api.dto.NhanVienDto;
import com.chauphat.qldatphong.domain.entity.NhanVien;
import org.mapstruct.Mapper;

@Mapper
public interface NhanVienMapper {
    NhanVienDto toDto(NhanVien entity);

    @org.mapstruct.Mapping(target = "password", ignore = true)
    NhanVien toEntity(NhanVienDto dto);
}
