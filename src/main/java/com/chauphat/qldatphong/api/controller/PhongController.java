package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.PhongDto;
import com.chauphat.qldatphong.api.mapper.PhongMapper;
import com.chauphat.qldatphong.common.ApiResponse;
import com.chauphat.qldatphong.domain.entity.Phong;
import com.chauphat.qldatphong.domain.service.PhongService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phong")
@RequiredArgsConstructor
@Validated
public class PhongController {
    private final PhongService service;
    private final PhongMapper mapper;

    public record UpsertPhongRequest(
            @NotNull Integer maLoaiPhong,
            @Size(max = 50) String tenPhong,
            Integer tang,
            @Size(max = 20) String trangThai
    ) {
    }

    @GetMapping
    public ApiResponse<Page<PhongDto>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable).map(mapper::toDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<PhongDto> get(@PathVariable Integer id) {
        return ApiResponse.ok(mapper.toDto(service.get(id)));
    }

    @PostMapping
    public ApiResponse<PhongDto> create(@Valid @RequestBody UpsertPhongRequest req) {
        Phong entity = Phong.builder()
                .loaiPhong(com.chauphat.qldatphong.domain.entity.LoaiPhong.builder().maLoaiPhong(req.maLoaiPhong()).build())
                .tenPhong(req.tenPhong())
                .tang(req.tang())
                .trangThai(req.trangThai())
                .build();
        return ApiResponse.ok(mapper.toDto(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ApiResponse<PhongDto> update(@PathVariable Integer id, @Valid @RequestBody UpsertPhongRequest req) {
        Phong patch = Phong.builder()
                .loaiPhong(com.chauphat.qldatphong.domain.entity.LoaiPhong.builder().maLoaiPhong(req.maLoaiPhong()).build())
                .tenPhong(req.tenPhong())
                .tang(req.tang())
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
