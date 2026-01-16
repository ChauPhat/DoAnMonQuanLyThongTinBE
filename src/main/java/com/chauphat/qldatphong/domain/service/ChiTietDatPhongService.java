package com.chauphat.qldatphong.domain.service;

import com.chauphat.qldatphong.domain.entity.ChiTietDatPhong;
import com.chauphat.qldatphong.domain.entity.DatPhong;
import com.chauphat.qldatphong.domain.entity.Phong;
import com.chauphat.qldatphong.domain.repository.ChiTietDatPhongRepository;
import com.chauphat.qldatphong.domain.repository.DatPhongRepository;
import com.chauphat.qldatphong.domain.repository.PhongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ChiTietDatPhongService {
    private final ChiTietDatPhongRepository repository;
    private final DatPhongRepository datPhongRepository;
    private final PhongRepository phongRepository;

    public Page<ChiTietDatPhong> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public ChiTietDatPhong get(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết đặt phòng: " + id));
    }

    public ChiTietDatPhong create(ChiTietDatPhong entity) {
        entity.setMaCtdp(null);

        Integer maDatPhong = entity.getDatPhong() != null ? entity.getDatPhong().getMaDatPhong() : null;
        Integer maPhong = entity.getPhong() != null ? entity.getPhong().getMaPhong() : null;
        if (maDatPhong == null) throw new IllegalArgumentException("MaDatPhong là bắt buộc");
        if (maPhong == null) throw new IllegalArgumentException("MaPhong là bắt buộc");

        DatPhong datPhong = datPhongRepository.findById(maDatPhong)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đặt phòng: " + maDatPhong));
        Phong phong = phongRepository.findById(maPhong)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng: " + maPhong));

        entity.setDatPhong(datPhong);
        entity.setPhong(phong);

        if (entity.getSoNgay() == null) {
            long days = ChronoUnit.DAYS.between(datPhong.getNgayNhan(), datPhong.getNgayTra());
            entity.setSoNgay((int) Math.max(1, days));
        }

        return repository.save(entity);
    }

    public ChiTietDatPhong update(Integer id, ChiTietDatPhong patch) {
        ChiTietDatPhong existing = get(id);

        Integer maDatPhong = patch.getDatPhong() != null ? patch.getDatPhong().getMaDatPhong() : null;
        Integer maPhong = patch.getPhong() != null ? patch.getPhong().getMaPhong() : null;

        if (maDatPhong != null) {
            DatPhong datPhong = datPhongRepository.findById(maDatPhong)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đặt phòng: " + maDatPhong));
            existing.setDatPhong(datPhong);
        }
        if (maPhong != null) {
            Phong phong = phongRepository.findById(maPhong)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng: " + maPhong));
            existing.setPhong(phong);
        }

        existing.setDonGia(patch.getDonGia());
        existing.setSoNgay(patch.getSoNgay());

        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.delete(get(id));
    }
}
