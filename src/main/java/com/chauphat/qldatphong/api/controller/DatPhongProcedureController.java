package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.PhongDto;
import com.chauphat.qldatphong.api.dto.procedure.DatPhongRequest;
import com.chauphat.qldatphong.api.dto.procedure.DoanhThuTheoThangRequest;
import com.chauphat.qldatphong.api.dto.procedure.ThemChiTietDatPhongRequest;
import com.chauphat.qldatphong.api.dto.procedure.TraPhongRequest;
import com.chauphat.qldatphong.common.ApiResponse;
import com.chauphat.qldatphong.domain.entity.Phong;
import com.chauphat.qldatphong.domain.service.procedure.DatPhongProcedureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/proc")
@RequiredArgsConstructor
public class DatPhongProcedureController {
    private final DatPhongProcedureService service;

    @PostMapping("/dat-phong")
    public ApiResponse<Integer> datPhong(@Valid @RequestBody DatPhongRequest req) {
        Integer maDatPhong = service.datPhong(req.maKh(), req.maNv(), req.ngayNhan(), req.ngayTra());
        return ApiResponse.ok(maDatPhong);
    }

    @PostMapping("/them-chi-tiet")
    public ApiResponse<Void> themChiTiet(@Valid @RequestBody ThemChiTietDatPhongRequest req) {
        service.themChiTietDatPhong(req.maDatPhong(), req.maPhong(), req.donGia());
        return ApiResponse.ok(null);
    }

    @PostMapping("/tra-phong")
    public ApiResponse<Void> traPhong(@Valid @RequestBody TraPhongRequest req) {
        service.traPhong(req.maDatPhong());
        return ApiResponse.ok(null);
    }

    @GetMapping("/phong-trong")
    public ApiResponse<List<PhongDto>> phongTrong() {
        List<Phong> rooms = service.phongTrong();
        List<PhongDto> dto = rooms.stream()
            .map(r -> new PhongDto(
                r.getMaPhong(),
                r.getLoaiPhong() != null ? r.getLoaiPhong().getMaLoaiPhong() : null,
                r.getTenPhong(),
                r.getTang(),
                r.getTrangThai()
            ))
                .toList();
        return ApiResponse.ok(dto);
    }

    @PostMapping("/doanh-thu")
    public ApiResponse<BigDecimal> doanhThu(@Valid @RequestBody DoanhThuTheoThangRequest req) {
        return ApiResponse.ok(service.tinhDoanhThuTheoThang(req.thang(), req.nam()));
    }
}
