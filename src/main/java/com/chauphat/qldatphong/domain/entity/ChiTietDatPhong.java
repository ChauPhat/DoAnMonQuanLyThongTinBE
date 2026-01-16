package com.chauphat.qldatphong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_dat_phong")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietDatPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTDP")
    private Integer maCtdp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaDatPhong", nullable = false)
    private DatPhong datPhong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaPhong", nullable = false)
    private Phong phong;

    @Column(name = "DonGia", precision = 18, scale = 2)
    private BigDecimal donGia;

    @Column(name = "SoNgay")
    private Integer soNgay;

    @Column(name = "ThanhTien", precision = 18, scale = 2)
    private BigDecimal thanhTien;
}
