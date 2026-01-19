package com.chauphat.qldatphong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "loai_phong")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoaiPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLoaiPhong")
    private Integer maLoaiPhong;

    @Column(name = "TenLoaiPhong", nullable = false, length = 100)
    private String tenLoaiPhong;

    @Column(name = "SucChua", nullable = false)
    private Integer sucChua;

    @Column(name = "GiaCoBan", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaCoBan;

    @Column(name = "MoTa", length = 255)
    private String moTa;
}
