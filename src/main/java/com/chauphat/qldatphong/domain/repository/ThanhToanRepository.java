package com.chauphat.qldatphong.domain.repository;

import com.chauphat.qldatphong.domain.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
	boolean existsByDatPhong_MaDatPhong(Integer maDatPhong);
}
