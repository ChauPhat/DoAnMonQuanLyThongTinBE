package com.chauphat.qldatphong.api.controller;

import com.chauphat.qldatphong.api.dto.PhongDto;
import com.chauphat.qldatphong.api.dto.procedure.BaoCaoDoanhThuCursorDto;
import com.chauphat.qldatphong.api.dto.procedure.DatPhongNhanhRequest;
import com.chauphat.qldatphong.api.dto.procedure.DatPhongSummaryDto;
import com.chauphat.qldatphong.api.dto.procedure.DoanhThuTheoThangRequest;
import com.chauphat.qldatphong.api.dto.procedure.NhanPhongRequest;
import com.chauphat.qldatphong.api.dto.procedure.PhongDaDatRequest;
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

    @GetMapping("/bao-cao-doanh-thu-cursor")
    public ApiResponse<List<BaoCaoDoanhThuCursorDto>> baoCaoDoanhThuCursor() {
        return ApiResponse.ok(service.baoCaoDoanhThuCursorReport());
    }

    @GetMapping("/danh-sach-dat-phong")
    public ApiResponse<List<DatPhongSummaryDto>> danhSachDatPhong(
            @RequestParam(name = "limit", defaultValue = "20") int limit,
            @RequestParam(name = "q", required = false) String q
    ) {
        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("limit phải trong khoảng 1..100");
        }
        return ApiResponse.ok(service.danhSachDatPhongGanDay(limit, q));
    }

    @GetMapping("/dat-phong/{maDatPhong}")
    public ApiResponse<DatPhongSummaryDto> thongTinDatPhong(@PathVariable Integer maDatPhong) {
        return ApiResponse.ok(service.thongTinDatPhong(maDatPhong));
    }

    @PostMapping("/dat-phong-nhanh")
    public ApiResponse<Integer> datPhongNhanh(@Valid @RequestBody DatPhongNhanhRequest req) {
        if (!req.ngayTra().isAfter(req.ngayNhan())) {
            throw new IllegalArgumentException("ngayTra phải lớn hơn ngayNhan");
        }

        Integer maDatPhong = service.datPhongNhanh(req.maKh(), req.maNv(), req.ngayNhan(), req.ngayTra(), req.maPhong());
        return ApiResponse.ok(maDatPhong);
    }

    @PostMapping("/tra-phong")
    public ApiResponse<Void> traPhong(@Valid @RequestBody TraPhongRequest req) {
        service.traPhong(req.maDatPhong());
        return ApiResponse.ok(null);
    }

    @PostMapping("/nhan-phong")
    public ApiResponse<Void> nhanPhong(@Valid @RequestBody NhanPhongRequest req) {
        service.nhanPhong(req.maDatPhong());
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

    @PostMapping("/phong-da-dat")
    public ApiResponse<List<PhongDto>> phongDaDat(@Valid @RequestBody PhongDaDatRequest req) {
        if (!req.denNgay().isAfter(req.tuNgay())) {
            throw new IllegalArgumentException("denNgay phải lớn hơn tuNgay");
        }

        List<Phong> rooms = service.phongDaDat(req.tuNgay(), req.denNgay());
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
