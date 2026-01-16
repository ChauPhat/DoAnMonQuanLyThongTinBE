package com.chauphat.qldatphong.api.mapper;

import com.chauphat.qldatphong.api.dto.DatPhongDto;
import com.chauphat.qldatphong.domain.entity.DatPhong;
import com.chauphat.qldatphong.domain.entity.KhachHang;
import com.chauphat.qldatphong.domain.entity.NhanVien;
import org.mapstruct.*;

@Mapper
public interface DatPhongMapper {

    @Mapping(target = "maKh", source = "khachHang.maKh")
    @Mapping(target = "maNv", source = "nhanVien.maNv")
    DatPhongDto toDto(DatPhong entity);

    @Mapping(target = "khachHang", source = "maKh", qualifiedByName = "toKhachHang")
    @Mapping(target = "nhanVien", source = "maNv", qualifiedByName = "toNhanVien")
    DatPhong toEntity(DatPhongDto dto);

    @Named("toKhachHang")
    default KhachHang toKhachHang(Integer maKh) {
        if (maKh == null) return null;
        return KhachHang.builder().maKh(maKh).build();
    }

    @Named("toNhanVien")
    default NhanVien toNhanVien(Integer maNv) {
        if (maNv == null) return null;
        return NhanVien.builder().maNv(maNv).build();
    }
}
