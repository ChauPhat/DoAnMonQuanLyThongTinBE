package com.chauphat.qldatphong.domain.service;

import com.chauphat.qldatphong.domain.entity.LoaiPhong;
import com.chauphat.qldatphong.domain.entity.Phong;
import com.chauphat.qldatphong.domain.repository.LoaiPhongRepository;
import com.chauphat.qldatphong.domain.repository.PhongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhongService {
    private final PhongRepository repository;
    private final LoaiPhongRepository loaiPhongRepository;

    public Page<Phong> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Phong get(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng: " + id));
    }

    public Phong create(Phong entity) {
        entity.setMaPhong(null);
        Integer maLoaiPhong = entity.getLoaiPhong() != null ? entity.getLoaiPhong().getMaLoaiPhong() : null;
        if (maLoaiPhong == null) throw new IllegalArgumentException("MaLoaiPhong là bắt buộc");
        LoaiPhong loaiPhong = loaiPhongRepository.findById(maLoaiPhong)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại phòng: " + maLoaiPhong));
        entity.setLoaiPhong(loaiPhong);
        return repository.save(entity);
    }

    public Phong update(Integer id, Phong patch) {
        Phong existing = get(id);

        Integer maLoaiPhong = patch.getLoaiPhong() != null ? patch.getLoaiPhong().getMaLoaiPhong() : null;
        if (maLoaiPhong != null) {
            LoaiPhong loaiPhong = loaiPhongRepository.findById(maLoaiPhong)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại phòng: " + maLoaiPhong));
            existing.setLoaiPhong(loaiPhong);
        }

        existing.setTenPhong(patch.getTenPhong());
        existing.setTang(patch.getTang());
        existing.setTrangThai(patch.getTrangThai());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.delete(get(id));
    }
}
