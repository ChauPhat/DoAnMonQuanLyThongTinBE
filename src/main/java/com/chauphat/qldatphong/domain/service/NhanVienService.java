package com.chauphat.qldatphong.domain.service;

import com.chauphat.qldatphong.domain.entity.NhanVien;
import com.chauphat.qldatphong.domain.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NhanVienService {
    private final NhanVienRepository repository;

    public Page<NhanVien> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public NhanVien get(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên: " + id));
    }

    public NhanVien create(NhanVien entity) {
        entity.setMaNv(null);
        if (entity.getUsername() != null && repository.existsByUsername(entity.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại: " + entity.getUsername());
        }
        return repository.save(entity);
    }

    public NhanVien update(Integer id, NhanVien patch) {
        NhanVien existing = get(id);

        if (patch.getUsername() != null && !patch.getUsername().equals(existing.getUsername())) {
            if (repository.existsByUsername(patch.getUsername())) {
                throw new IllegalArgumentException("Username đã tồn tại: " + patch.getUsername());
            }
            existing.setUsername(patch.getUsername());
        }

        existing.setTenNv(patch.getTenNv());
        existing.setVaiTro(patch.getVaiTro());

        // For simplicity, allow updating password (null means keep old)
        if (patch.getPassword() != null) {
            existing.setPassword(patch.getPassword());
        }

        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.delete(get(id));
    }
}
