package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.KhachHangDto;
import com.chauphat.qldatphong.api.mapper.KhachHangMapper;
import com.chauphat.qldatphong.common.ApiResponse;
import com.chauphat.qldatphong.domain.entity.KhachHang;
import com.chauphat.qldatphong.domain.service.KhachHangService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
@Validated
public class KhachHangController {
    private final KhachHangService service;
    private final KhachHangMapper mapper;

    public record UpsertKhachHangRequest(
            @NotBlank @Size(max = 100) String hoTen,
            @Size(max = 20) String cccd,
            @Size(max = 15) String dienThoai,
            @Size(max = 100) String email,
            @Size(max = 255) String diaChi
    ) {
    }

    @GetMapping
    public ApiResponse<Page<KhachHangDto>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable).map(mapper::toDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<KhachHangDto> get(@PathVariable Integer id) {
        return ApiResponse.ok(mapper.toDto(service.get(id)));
    }

    @PostMapping
    public ApiResponse<KhachHangDto> create(@Valid @RequestBody UpsertKhachHangRequest req) {
        KhachHang entity = KhachHang.builder()
                .hoTen(req.hoTen())
                .cccd(req.cccd())
                .dienThoai(req.dienThoai())
                .email(req.email())
                .diaChi(req.diaChi())
                .build();
        return ApiResponse.ok(mapper.toDto(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ApiResponse<KhachHangDto> update(@PathVariable Integer id, @Valid @RequestBody UpsertKhachHangRequest req) {
        KhachHang patch = KhachHang.builder()
                .hoTen(req.hoTen())
                .cccd(req.cccd())
                .dienThoai(req.dienThoai())
                .email(req.email())
                .diaChi(req.diaChi())
                .build();
        return ApiResponse.ok(mapper.toDto(service.update(id, patch)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ApiResponse.ok(null);
    }
}
