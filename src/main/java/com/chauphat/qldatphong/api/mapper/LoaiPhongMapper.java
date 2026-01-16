package com.chauphat.qldatphong.api.mapper;

import com.chauphat.qldatphong.api.dto.LoaiPhongDto;
import com.chauphat.qldatphong.domain.entity.LoaiPhong;
import org.mapstruct.Mapper;

@Mapper
public interface LoaiPhongMapper {
    LoaiPhongDto toDto(LoaiPhong entity);

    LoaiPhong toEntity(LoaiPhongDto dto);
}
