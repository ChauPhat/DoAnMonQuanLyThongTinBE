package com.chauphat.qldatphong.domain.service;

import com.chauphat.qldatphong.domain.entity.DatPhong;
import com.chauphat.qldatphong.domain.entity.KhachHang;
import com.chauphat.qldatphong.domain.entity.NhanVien;
import com.chauphat.qldatphong.domain.repository.DatPhongRepository;
import com.chauphat.qldatphong.domain.repository.KhachHangRepository;
import com.chauphat.qldatphong.domain.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DatPhongService {
    private final DatPhongRepository repository;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;

    public Page<DatPhong> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public DatPhong get(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đặt phòng: " + id));
    }

    public DatPhong create(DatPhong entity) {
        entity.setMaDatPhong(null);

        Integer maKh = entity.getKhachHang() != null ? entity.getKhachHang().getMaKh() : null;
        Integer maNv = entity.getNhanVien() != null ? entity.getNhanVien().getMaNv() : null;
        if (maKh == null) throw new IllegalArgumentException("MaKH là bắt buộc");
        if (maNv == null) throw new IllegalArgumentException("MaNV là bắt buộc");

        KhachHang kh = khachHangRepository.findById(maKh)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng: " + maKh));
        NhanVien nv = nhanVienRepository.findById(maNv)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên: " + maNv));

        entity.setKhachHang(kh);
        entity.setNhanVien(nv);

        if (entity.getNgayDat() == null) {
            entity.setNgayDat(LocalDateTime.now());
        }
        if (entity.getNgayNhan() == null) throw new IllegalArgumentException("NgayNhan là bắt buộc");
        if (entity.getNgayTra() == null) throw new IllegalArgumentException("NgayTra là bắt buộc");
        if (!entity.getNgayTra().isAfter(entity.getNgayNhan())) {
            throw new IllegalArgumentException("NgayTra phải lớn hơn NgayNhan");
        }
        if (entity.getTrangThai() == null || entity.getTrangThai().isBlank()) {
            entity.setTrangThai("đã đặt");
        }

        return repository.save(entity);
    }

    public DatPhong update(Integer id, DatPhong patch) {
        DatPhong existing = get(id);

        Integer maKh = patch.getKhachHang() != null ? patch.getKhachHang().getMaKh() : null;
        Integer maNv = patch.getNhanVien() != null ? patch.getNhanVien().getMaNv() : null;

        if (maKh != null) {
            KhachHang kh = khachHangRepository.findById(maKh)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng: " + maKh));
            existing.setKhachHang(kh);
        }
        if (maNv != null) {
            NhanVien nv = nhanVienRepository.findById(maNv)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên: " + maNv));
            existing.setNhanVien(nv);
        }

        if (patch.getNgayDat() != null) {
            existing.setNgayDat(patch.getNgayDat());
        }
        existing.setNgayNhan(patch.getNgayNhan());
        existing.setNgayTra(patch.getNgayTra());
        existing.setTrangThai(patch.getTrangThai());

        if (existing.getNgayNhan() == null) throw new IllegalArgumentException("NgayNhan là bắt buộc");
        if (existing.getNgayTra() == null) throw new IllegalArgumentException("NgayTra là bắt buộc");
        if (!existing.getNgayTra().isAfter(existing.getNgayNhan())) {
            throw new IllegalArgumentException("NgayTra phải lớn hơn NgayNhan");
        }

        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.delete(get(id));
    }
}
