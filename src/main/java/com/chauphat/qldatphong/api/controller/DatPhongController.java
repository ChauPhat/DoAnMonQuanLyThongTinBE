package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.DatPhongDto;
import com.chauphat.qldatphong.api.mapper.DatPhongMapper;
import com.chauphat.qldatphong.common.ApiResponse;
import com.chauphat.qldatphong.domain.entity.DatPhong;
import com.chauphat.qldatphong.domain.entity.KhachHang;
import com.chauphat.qldatphong.domain.entity.NhanVien;
import com.chauphat.qldatphong.domain.service.DatPhongService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dat-phong")
@RequiredArgsConstructor
@Validated
public class DatPhongController {
    private final DatPhongService service;
    private final DatPhongMapper mapper;

    public record UpsertDatPhongRequest(
            @NotNull Integer maKh,
            @NotNull Integer maNv,
            LocalDateTime ngayDat,
            @NotNull LocalDateTime ngayNhan,
            @NotNull LocalDateTime ngayTra,
            @Size(max = 30) String trangThai
    ) {
    }

    @GetMapping
    public ApiResponse<Page<DatPhongDto>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable).map(mapper::toDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<DatPhongDto> get(@PathVariable Integer id) {
        return ApiResponse.ok(mapper.toDto(service.get(id)));
    }

    @PostMapping
    public ApiResponse<DatPhongDto> create(@Valid @RequestBody UpsertDatPhongRequest req) {
        DatPhong entity = DatPhong.builder()
                .khachHang(KhachHang.builder().maKh(req.maKh()).build())
                .nhanVien(NhanVien.builder().maNv(req.maNv()).build())
                .ngayDat(req.ngayDat())
                .ngayNhan(req.ngayNhan())
                .ngayTra(req.ngayTra())
                .trangThai(req.trangThai())
                .build();
        return ApiResponse.ok(mapper.toDto(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ApiResponse<DatPhongDto> update(@PathVariable Integer id, @Valid @RequestBody UpsertDatPhongRequest req) {
        DatPhong patch = DatPhong.builder()
                .khachHang(KhachHang.builder().maKh(req.maKh()).build())
                .nhanVien(NhanVien.builder().maNv(req.maNv()).build())
                .ngayDat(req.ngayDat())
                .ngayNhan(req.ngayNhan())
                .ngayTra(req.ngayTra())
                .trangThai(req.trangThai())
                .build();
        return ApiResponse.ok(mapper.toDto(service.update(id, patch)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ApiResponse.ok(null);
    }
}
