package com.chauphat.qldatphong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nhan_vien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNV")
    private Integer maNv;

    @Column(name = "TenNV", nullable = false, length = 100)
    private String tenNv;

    @Column(name = "VaiTro", nullable = false, length = 50)
    private String vaiTro;

    @Column(name = "Username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;
}
