package com.chauphat.qldatphong.domain.service;

import com.chauphat.qldatphong.domain.entity.LoaiPhong;
import com.chauphat.qldatphong.domain.repository.LoaiPhongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoaiPhongService {
    private final LoaiPhongRepository repository;

    public Page<LoaiPhong> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public LoaiPhong get(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại phòng: " + id));
    }

    public LoaiPhong create(LoaiPhong entity) {
        entity.setMaLoaiPhong(null);
        return repository.save(entity);
    }

    public LoaiPhong update(Integer id, LoaiPhong patch) {
        LoaiPhong existing = get(id);
        existing.setTenLoaiPhong(patch.getTenLoaiPhong());
        existing.setSucChua(patch.getSucChua());
        existing.setGiaCoBan(patch.getGiaCoBan());
        existing.setMoTa(patch.getMoTa());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.delete(get(id));
    }
}
