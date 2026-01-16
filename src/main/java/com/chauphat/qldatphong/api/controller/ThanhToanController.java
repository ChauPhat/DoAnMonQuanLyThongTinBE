package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.ThanhToanDto;
import com.chauphat.qldatphong.api.mapper.ThanhToanMapper;
import com.chauphat.qldatphong.common.ApiResponse;
import com.chauphat.qldatphong.domain.entity.DatPhong;
import com.chauphat.qldatphong.domain.entity.ThanhToan;
import com.chauphat.qldatphong.domain.service.ThanhToanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/thanh-toan")
@RequiredArgsConstructor
@Validated
public class ThanhToanController {
    private final ThanhToanService service;
    private final ThanhToanMapper mapper;

    public record UpsertThanhToanRequest(
            @NotNull Integer maDatPhong,
            LocalDateTime ngayThanhToan,
            @NotNull BigDecimal soTien,
            @Size(max = 50) String phuongThuc,
            @Size(max = 30) String trangThai
    ) {
    }

    @GetMapping
    public ApiResponse<Page<ThanhToanDto>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable).map(mapper::toDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<ThanhToanDto> get(@PathVariable Integer id) {
        return ApiResponse.ok(mapper.toDto(service.get(id)));
    }

    @PostMapping
    public ApiResponse<ThanhToanDto> create(@Valid @RequestBody UpsertThanhToanRequest req) {
        ThanhToan entity = ThanhToan.builder()
                .datPhong(DatPhong.builder().maDatPhong(req.maDatPhong()).build())
                .ngayThanhToan(req.ngayThanhToan())
                .soTien(req.soTien())
                .phuongThuc(req.phuongThuc())
                .trangThai(req.trangThai())
                .build();
        return ApiResponse.ok(mapper.toDto(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ApiResponse<ThanhToanDto> update(@PathVariable Integer id, @Valid @RequestBody UpsertThanhToanRequest req) {
        ThanhToan patch = ThanhToan.builder()
                .datPhong(DatPhong.builder().maDatPhong(req.maDatPhong()).build())
                .ngayThanhToan(req.ngayThanhToan())
                .soTien(req.soTien())
                .phuongThuc(req.phuongThuc())
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
