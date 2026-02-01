/*
  Reports (5) - Stored Procedures returning result sets
  This file also grants EXECUTE to role_report.
*/

use ql_dat_phong;
go

-- 1) Report: Doanh thu theo nam (group by thang)
create or alter procedure dbo.sp_report_doanhThuTheoNam
    @Nam int
as
begin
    set nocount on;

    select
        month(tt.NgayThanhToan) as Thang,
        @Nam as Nam,
        sum(isnull(tt.SoTien, 0)) as TongDoanhThu
    from dbo.thanh_toan tt
    where tt.NgayThanhToan is not null
      and year(tt.NgayThanhToan) = @Nam
      and tt.TrangThai = N'Đã thanh toán'
    group by month(tt.NgayThanhToan)
    order by Thang;
end;
go

-- 2) Report: Top khach hang theo tong chi tieu
create or alter procedure dbo.sp_report_topKhachHang
    @Top int = 10
as
begin
    set nocount on;

    if (@Top is null or @Top < 1) set @Top = 10;
    if (@Top > 100) set @Top = 100;

    select top (@Top)
        kh.MaKH,
        kh.HoTen,
        count(distinct dp.MaDatPhong) as SoLanDat,
        sum(isnull(tt.SoTien, 0)) as TongChiTieu
    from dbo.khach_hang kh
    join dbo.dat_phong dp on dp.MaKH = kh.MaKH
    left join dbo.thanh_toan tt on tt.MaDatPhong = dp.MaDatPhong
    where tt.TrangThai = N'Đã thanh toán'
    group by kh.MaKH, kh.HoTen
    order by TongChiTieu desc;
end;
go

-- 3) Report: Thong ke dat phong theo nhan vien trong khoang thoi gian
create or alter procedure dbo.sp_report_datPhongTheoNhanVien
    @TuNgay datetime,
    @DenNgay datetime
as
begin
    set nocount on;

    if (@TuNgay is null or @DenNgay is null or @DenNgay <= @TuNgay)
    begin
        raiserror(N'Khoảng ngày không hợp lệ', 16, 1);
        return;
    end

    select
        nv.MaNV,
        nv.TenNV,
        count(distinct dp.MaDatPhong) as SoDonDat,
        sum(isnull(tt.SoTien, 0)) as TongThu
    from dbo.nhan_vien nv
    left join dbo.dat_phong dp on dp.MaNV = nv.MaNV
        and dp.NgayDat >= @TuNgay
        and dp.NgayDat < @DenNgay
    left join dbo.thanh_toan tt on tt.MaDatPhong = dp.MaDatPhong
        and tt.TrangThai = N'Đã thanh toán'
    group by nv.MaNV, nv.TenNV
    order by SoDonDat desc, TongThu desc;
end;
go

-- 4) Report: Cong no / chua thanh toan
create or alter procedure dbo.sp_report_congNo
as
begin
    set nocount on;

    select
        dp.MaDatPhong,
        dp.NgayDat,
        kh.MaKH,
        kh.HoTen as HoTenKH,
        dp.TrangThai as TrangThaiDatPhong,
        dbo.fn_tinhTongTienDatPhong(dp.MaDatPhong) as TongTienTheoCT,
        isnull(tt.SoTien, 0) as SoTienThanhToan,
        isnull(tt.TrangThai, N'Chưa thanh toán') as TrangThaiThanhToan
    from dbo.dat_phong dp
    join dbo.khach_hang kh on kh.MaKH = dp.MaKH
    left join dbo.thanh_toan tt on tt.MaDatPhong = dp.MaDatPhong
    where isnull(tt.TrangThai, N'Chưa thanh toán') <> N'Đã thanh toán'
    order by dp.MaDatPhong desc;
end;
go

-- 5) Report: Cong suat / tinh trang phong theo khoang thoi gian
create or alter procedure dbo.sp_report_congSuatPhong
    @TuNgay datetime,
    @DenNgay datetime
as
begin
    set nocount on;

    if (@TuNgay is null or @DenNgay is null or @DenNgay <= @TuNgay)
    begin
        raiserror(N'Khoảng ngày không hợp lệ', 16, 1);
        return;
    end

    ;with booked as (
        select distinct ct.MaPhong
        from dbo.chi_tiet_dat_phong ct
        join dbo.dat_phong dp on dp.MaDatPhong = ct.MaDatPhong
        where dp.TrangThai in (N'đã đặt', N'đã nhận phòng')
          and dp.NgayNhan < @DenNgay
          and dp.NgayTra > @TuNgay
    )
    select
        p.MaPhong,
        p.TenPhong,
        p.MaLoaiPhong,
        p.TrangThai as TrangThaiHienTai,
        case when b.MaPhong is not null then cast(1 as bit) else cast(0 as bit) end as IsDaDuocDatTrongKhoang,
        dbo.fn_isPhongTrongTheoKhoang(p.MaPhong, @TuNgay, @DenNgay) as IsTrongTheoKhoang
    from dbo.phong p
    left join booked b on b.MaPhong = p.MaPhong
    order by p.MaPhong;
end;
go

-- ===== Grants for report role =====
grant execute on dbo.sp_report_doanhThuTheoNam to role_report;
grant execute on dbo.sp_report_topKhachHang to role_report;
grant execute on dbo.sp_report_datPhongTheoNhanVien to role_report;
grant execute on dbo.sp_report_congNo to role_report;
grant execute on dbo.sp_report_congSuatPhong to role_report;
go

-- Optional: allow staff to run reports too
grant execute on dbo.sp_report_doanhThuTheoNam to role_staff;
grant execute on dbo.sp_report_topKhachHang to role_staff;
grant execute on dbo.sp_report_datPhongTheoNhanVien to role_staff;
grant execute on dbo.sp_report_congNo to role_staff;
grant execute on dbo.sp_report_congSuatPhong to role_staff;
go
