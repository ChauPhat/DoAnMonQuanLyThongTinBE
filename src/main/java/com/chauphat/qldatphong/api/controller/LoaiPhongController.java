package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.LoaiPhongDto;
import com.chauphat.qldatphong.api.mapper.LoaiPhongMapper;
import com.chauphat.qldatphong.common.ApiResponse;
import com.chauphat.qldatphong.domain.entity.LoaiPhong;
import com.chauphat.qldatphong.domain.service.LoaiPhongService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/loai-phong")
@RequiredArgsConstructor
@Validated
public class LoaiPhongController {
    private final LoaiPhongService service;
    private final LoaiPhongMapper mapper;

    public record UpsertLoaiPhongRequest(
            @NotBlank @Size(max = 100) String tenLoaiPhong,
            @NotNull @Positive Integer sucChua,
            @NotNull @Positive BigDecimal giaCoBan,
            @Size(max = 255) String moTa
    ) {
    }

    @GetMapping
    public ApiResponse<Page<LoaiPhongDto>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable).map(mapper::toDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<LoaiPhongDto> get(@PathVariable Integer id) {
        return ApiResponse.ok(mapper.toDto(service.get(id)));
    }

    @PostMapping
    public ApiResponse<LoaiPhongDto> create(@Valid @RequestBody UpsertLoaiPhongRequest req) {
        LoaiPhong entity = LoaiPhong.builder()
                .tenLoaiPhong(req.tenLoaiPhong())
                .sucChua(req.sucChua())
                .giaCoBan(req.giaCoBan())
                .moTa(req.moTa())
                .build();
        return ApiResponse.ok(mapper.toDto(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ApiResponse<LoaiPhongDto> update(@PathVariable Integer id, @Valid @RequestBody UpsertLoaiPhongRequest req) {
        LoaiPhong patch = LoaiPhong.builder()
                .tenLoaiPhong(req.tenLoaiPhong())
                .sucChua(req.sucChua())
                .giaCoBan(req.giaCoBan())
                .moTa(req.moTa())
                .build();
        return ApiResponse.ok(mapper.toDto(service.update(id, patch)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ApiResponse.ok(null);
    }
}
