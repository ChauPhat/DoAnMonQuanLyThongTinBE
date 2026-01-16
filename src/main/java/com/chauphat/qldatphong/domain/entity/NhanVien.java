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

    @Column(name = "TenNV", length = 100)
    private String tenNv;

    @Column(name = "VaiTro", length = 50)
    private String vaiTro;

    @Column(name = "Username", length = 50, unique = true)
    private String username;

    @Column(name = "Password", length = 255)
    private String password;
}
