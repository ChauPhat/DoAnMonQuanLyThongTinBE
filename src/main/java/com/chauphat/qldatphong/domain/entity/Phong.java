package com.chauphat.qldatphong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phong")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhong")
    private Integer maPhong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaLoaiPhong", nullable = false)
    private LoaiPhong loaiPhong;

    @Column(name = "TenPhong", length = 50)
    private String tenPhong;

    @Column(name = "Tang")
    private Integer tang;

    @Column(name = "TrangThai", length = 20)
    private String trangThai;
}
