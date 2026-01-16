package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.ChiTietDatPhongDto;
import com.chauphat.qldatphong.api.mapper.ChiTietDatPhongMapper;
import com.chauphat.qldatphong.common.ApiResponse;
import com.chauphat.qldatphong.domain.entity.ChiTietDatPhong;
import com.chauphat.qldatphong.domain.entity.DatPhong;
import com.chauphat.qldatphong.domain.entity.Phong;
import com.chauphat.qldatphong.domain.service.ChiTietDatPhongService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/chi-tiet-dat-phong")
@RequiredArgsConstructor
@Validated
public class ChiTietDatPhongController {
    private final ChiTietDatPhongService service;
    private final ChiTietDatPhongMapper mapper;

    public record UpsertChiTietDatPhongRequest(
            @NotNull Integer maDatPhong,
            @NotNull Integer maPhong,
            @NotNull BigDecimal donGia,
            Integer soNgay
    ) {
    }

    @GetMapping
    public ApiResponse<Page<ChiTietDatPhongDto>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable).map(mapper::toDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<ChiTietDatPhongDto> get(@PathVariable Integer id) {
        return ApiResponse.ok(mapper.toDto(service.get(id)));
    }

    @PostMapping
    public ApiResponse<ChiTietDatPhongDto> create(@Valid @RequestBody UpsertChiTietDatPhongRequest req) {
        ChiTietDatPhong entity = ChiTietDatPhong.builder()
                .datPhong(DatPhong.builder().maDatPhong(req.maDatPhong()).build())
                .phong(Phong.builder().maPhong(req.maPhong()).build())
                .donGia(req.donGia())
                .soNgay(req.soNgay())
                .build();
        return ApiResponse.ok(mapper.toDto(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ApiResponse<ChiTietDatPhongDto> update(@PathVariable Integer id, @Valid @RequestBody UpsertChiTietDatPhongRequest req) {
        ChiTietDatPhong patch = ChiTietDatPhong.builder()
                .datPhong(DatPhong.builder().maDatPhong(req.maDatPhong()).build())
                .phong(Phong.builder().maPhong(req.maPhong()).build())
                .donGia(req.donGia())
                .soNgay(req.soNgay())
                .build();
        return ApiResponse.ok(mapper.toDto(service.update(id, patch)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ApiResponse.ok(null);
    }
}
