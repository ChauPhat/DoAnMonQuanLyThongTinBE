package com.chauphat.qldatphong.api.mapper;

import com.chauphat.qldatphong.api.dto.ChiTietDatPhongDto;
import com.chauphat.qldatphong.domain.entity.*;
import org.mapstruct.*;

@Mapper
public interface ChiTietDatPhongMapper {

    @Mapping(target = "maDatPhong", source = "datPhong.maDatPhong")
    @Mapping(target = "maPhong", source = "phong.maPhong")
    ChiTietDatPhongDto toDto(ChiTietDatPhong entity);

    @Mapping(target = "datPhong", source = "maDatPhong", qualifiedByName = "toDatPhong")
    @Mapping(target = "phong", source = "maPhong", qualifiedByName = "toPhong")
    ChiTietDatPhong toEntity(ChiTietDatPhongDto dto);

    @Named("toDatPhong")
    default DatPhong toDatPhong(Integer maDatPhong) {
        if (maDatPhong == null) return null;
        return DatPhong.builder().maDatPhong(maDatPhong).build();
    }

    @Named("toPhong")
    default Phong toPhong(Integer maPhong) {
        if (maPhong == null) return null;
        return Phong.builder().maPhong(maPhong).build();
    }
}
