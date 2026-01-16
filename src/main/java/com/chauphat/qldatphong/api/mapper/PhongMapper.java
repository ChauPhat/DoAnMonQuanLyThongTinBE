package com.chauphat.qldatphong.api.mapper;

import com.chauphat.qldatphong.api.dto.PhongDto;
import com.chauphat.qldatphong.domain.entity.LoaiPhong;
import com.chauphat.qldatphong.domain.entity.Phong;
import org.mapstruct.*;

@Mapper
public interface PhongMapper {

    @Mapping(target = "maLoaiPhong", source = "loaiPhong.maLoaiPhong")
    PhongDto toDto(Phong entity);

    @Mapping(target = "loaiPhong", source = "maLoaiPhong", qualifiedByName = "toLoaiPhong")
    Phong toEntity(PhongDto dto);

    @Named("toLoaiPhong")
    default LoaiPhong toLoaiPhong(Integer maLoaiPhong) {
        if (maLoaiPhong == null) return null;
        return LoaiPhong.builder().maLoaiPhong(maLoaiPhong).build();
    }
}
