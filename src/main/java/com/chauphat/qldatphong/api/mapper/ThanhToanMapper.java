package com.chauphat.qldatphong.api.mapper;

import com.chauphat.qldatphong.api.dto.ThanhToanDto;
import com.chauphat.qldatphong.domain.entity.DatPhong;
import com.chauphat.qldatphong.domain.entity.ThanhToan;
import org.mapstruct.*;

@Mapper
public interface ThanhToanMapper {

    @Mapping(target = "maDatPhong", source = "datPhong.maDatPhong")
    ThanhToanDto toDto(ThanhToan entity);

    @Mapping(target = "datPhong", source = "maDatPhong", qualifiedByName = "toDatPhong")
    ThanhToan toEntity(ThanhToanDto dto);

    @Named("toDatPhong")
    default DatPhong toDatPhong(Integer maDatPhong) {
        if (maDatPhong == null) return null;
        return DatPhong.builder().maDatPhong(maDatPhong).build();
    }
}
