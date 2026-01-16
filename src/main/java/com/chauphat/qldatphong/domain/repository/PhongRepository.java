package com.chauphat.qldatphong.domain.repository;

import com.chauphat.qldatphong.domain.entity.Phong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhongRepository extends JpaRepository<Phong, Integer> {
    List<Phong> findByTrangThai(String trangThai);
}
