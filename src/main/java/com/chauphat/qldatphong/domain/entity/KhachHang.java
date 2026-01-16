package com.chauphat.qldatphong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKH")
    private Integer maKh;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "CCCD", length = 20, unique = true)
    private String cccd;

    @Column(name = "DienThoai", length = 15)
    private String dienThoai;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;
}
