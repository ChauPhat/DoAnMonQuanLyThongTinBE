package com.chauphat.qldatphong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dat_phong")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDatPhong")
    private Integer maDatPhong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaNV", nullable = false)
    private NhanVien nhanVien;

    @Column(name = "NgayDat")
    private LocalDateTime ngayDat;

    @Column(name = "NgayNhan", nullable = false)
    private LocalDateTime ngayNhan;

    @Column(name = "NgayTra", nullable = false)
    private LocalDateTime ngayTra;

    @Column(name = "TrangThai", length = 30)
    private String trangThai;
}
