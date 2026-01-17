package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.NhanVienDto;
import com.chauphat.qldatphong.api.mapper.NhanVienMapper;
import com.chauphat.qldatphong.common.ApiResponse;
import com.chauphat.qldatphong.domain.entity.NhanVien;
import com.chauphat.qldatphong.domain.service.NhanVienService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nhan-vien")
@RequiredArgsConstructor
@Validated
public class NhanVienController {
    private final NhanVienService service;
    private final NhanVienMapper mapper;

    public record CreateNhanVienRequest(
            @NotBlank @Size(max = 100) String tenNv,
            @NotBlank @Size(max = 50) String vaiTro,
            @NotBlank @Size(max = 50) String username,
            @NotBlank @Size(max = 255) String password
    ) {
    }

    public record UpdateNhanVienRequest(
            @Size(max = 100) String tenNv,
            @Size(max = 50) String vaiTro,
            @NotBlank @Size(max = 50) String username,
            @Size(max = 255) String password
    ) {
    }

    @GetMapping
    public ApiResponse<Page<NhanVienDto>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable).map(mapper::toDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<NhanVienDto> get(@PathVariable Integer id) {
        return ApiResponse.ok(mapper.toDto(service.get(id)));
    }

    @PostMapping
    public ApiResponse<NhanVienDto> create(@Valid @RequestBody CreateNhanVienRequest req) {
        NhanVien entity = NhanVien.builder()
                .tenNv(req.tenNv())
                .vaiTro(req.vaiTro())
                .username(req.username())
                .password(req.password())
                .build();
        return ApiResponse.ok(mapper.toDto(service.create(entity)));
    }

    @PutMapping("/{id}")
    public ApiResponse<NhanVienDto> update(@PathVariable Integer id, @Valid @RequestBody UpdateNhanVienRequest req) {
        NhanVien patch = NhanVien.builder()
                .tenNv(req.tenNv())
                .vaiTro(req.vaiTro())
                .username(req.username())
                .password(req.password())
                .build();
        return ApiResponse.ok(mapper.toDto(service.update(id, patch)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ApiResponse.ok(null);
    }
}
