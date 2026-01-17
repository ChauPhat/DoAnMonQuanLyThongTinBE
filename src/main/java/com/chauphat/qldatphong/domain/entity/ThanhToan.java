package com.chauphat.qldatphong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThanhToan")
    private Integer maThanhToan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDatPhong", nullable = false, unique = true)
    private DatPhong datPhong;

    @Column(name = "NgayThanhToan")
    private LocalDateTime ngayThanhToan;

    @Column(name = "SoTien", precision = 18, scale = 2)
    private BigDecimal soTien;

    @Column(name = "PhuongThuc", length = 50)
    private String phuongThuc;

    @Column(name = "TrangThai", length = 30)
    private String trangThai;
}
