package com.chauphat.qldatphong.domain.service;

import com.chauphat.qldatphong.domain.entity.DatPhong;
import com.chauphat.qldatphong.domain.entity.ThanhToan;
import com.chauphat.qldatphong.domain.repository.DatPhongRepository;
import com.chauphat.qldatphong.domain.repository.ThanhToanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ThanhToanService {
    private final ThanhToanRepository repository;
    private final DatPhongRepository datPhongRepository;

    public Page<ThanhToan> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public ThanhToan get(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thanh toán: " + id));
    }

    public ThanhToan create(ThanhToan entity) {
        entity.setMaThanhToan(null);

        Integer maDatPhong = entity.getDatPhong() != null ? entity.getDatPhong().getMaDatPhong() : null;
        if (maDatPhong == null) throw new IllegalArgumentException("MaDatPhong là bắt buộc");

        if (repository.existsByDatPhong_MaDatPhong(maDatPhong)) {
            throw new IllegalArgumentException("Đặt phòng đã có thanh toán: " + maDatPhong);
        }

        DatPhong datPhong = datPhongRepository.findById(maDatPhong)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đặt phòng: " + maDatPhong));
        entity.setDatPhong(datPhong);

        if (entity.getNgayThanhToan() == null) {
            entity.setNgayThanhToan(LocalDateTime.now());
        }

        return repository.save(entity);
    }

    public ThanhToan update(Integer id, ThanhToan patch) {
        ThanhToan existing = get(id);

        Integer maDatPhong = patch.getDatPhong() != null ? patch.getDatPhong().getMaDatPhong() : null;
        if (maDatPhong != null) {
            if (!maDatPhong.equals(existing.getDatPhong() != null ? existing.getDatPhong().getMaDatPhong() : null)
                    && repository.existsByDatPhong_MaDatPhong(maDatPhong)) {
                throw new IllegalArgumentException("Đặt phòng đã có thanh toán: " + maDatPhong);
            }
            DatPhong datPhong = datPhongRepository.findById(maDatPhong)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đặt phòng: " + maDatPhong));
            existing.setDatPhong(datPhong);
        }

        existing.setNgayThanhToan(patch.getNgayThanhToan());
        existing.setSoTien(patch.getSoTien());
        existing.setPhuongThuc(patch.getPhuongThuc());
        existing.setTrangThai(patch.getTrangThai());

        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.delete(get(id));
    }
}
