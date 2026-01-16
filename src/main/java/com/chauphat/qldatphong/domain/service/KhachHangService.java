package com.chauphat.qldatphong.domain.service;

import com.chauphat.qldatphong.domain.entity.KhachHang;
import com.chauphat.qldatphong.domain.repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KhachHangService {
    private final KhachHangRepository repository;

    public Page<KhachHang> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public KhachHang get(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng: " + id));
    }

    public KhachHang create(KhachHang entity) {
        entity.setMaKh(null);
        return repository.save(entity);
    }

    public KhachHang update(Integer id, KhachHang patch) {
        KhachHang existing = get(id);
        existing.setHoTen(patch.getHoTen());
        existing.setCccd(patch.getCccd());
        existing.setDienThoai(patch.getDienThoai());
        existing.setEmail(patch.getEmail());
        existing.setDiaChi(patch.getDiaChi());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.delete(get(id));
    }
}
