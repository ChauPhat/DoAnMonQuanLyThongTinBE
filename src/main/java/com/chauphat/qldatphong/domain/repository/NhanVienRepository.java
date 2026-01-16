package com.chauphat.qldatphong.domain.repository;

import com.chauphat.qldatphong.domain.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
	boolean existsByUsername(String username);
}
