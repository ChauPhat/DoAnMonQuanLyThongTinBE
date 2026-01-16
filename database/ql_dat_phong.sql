if exists (select name from sys.databases where name = 'ql_dat_phong')
begin
    drop database ql_dat_phong;
end;
go

create database ql_dat_phong;
go
use ql_dat_phong;
go

create table khach_hang (
    MaKH int identity primary key,
    HoTen nvarchar(100) not null,
    CCCD varchar(20) unique,
    DienThoai varchar(15),
    Email varchar(100),
    DiaChi nvarchar(255)
);
go

create table loai_phong (
    MaLoaiPhong int identity primary key,
    TenLoaiPhong nvarchar(100) not null,
    SucChua int check (SucChua > 0),
    GiaCoBan decimal(18,2) check (GiaCoBan > 0),
    MoTa nvarchar(255)
);
go

create table phong (
    MaPhong int identity primary key,
    MaLoaiPhong int not null,
    TenPhong nvarchar(50),
    Tang int,
    TrangThai nvarchar(20)
        check (TrangThai in (N'trống', N'đang thuê', N'bảo trì')),
    foreign key (MaLoaiPhong) references loai_phong(MaLoaiPhong)
);
go

create table nhan_vien (
    MaNV int identity primary key,
    TenNV nvarchar(100),
    VaiTro nvarchar(50),
    Username varchar(50) unique,
    Password varchar(255)
);
go

create table dat_phong (
    MaDatPhong int identity primary key,
    MaKH int not null,
    MaNV int not null,
    NgayDat datetime default getdate(),
    NgayNhan datetime not null,
    NgayTra datetime not null,
    TrangThai nvarchar(30)
        check (TrangThai in (N'đã đặt', N'đã nhận phòng', N'hủy')),
    foreign key (MaKH) references khach_hang(MaKH),
    foreign key (MaNV) references nhan_vien(MaNV),
    check (NgayTra > NgayNhan)
);
go

create table chi_tiet_dat_phong (
    MaCTDP int identity primary key,
    MaDatPhong int not null,
    MaPhong int not null,
    DonGia decimal(18,2),
    SoNgay int check (SoNgay > 0),
    ThanhTien decimal(18,2),
    foreign key (MaDatPhong) references dat_phong(MaDatPhong),
    foreign key (MaPhong) references phong(MaPhong)
);
go

create table thanh_toan (
    MaThanhToan int identity primary key,
    MaDatPhong int unique,
    NgayThanhToan datetime,
    SoTien decimal(18,2),
    PhuongThuc nvarchar(50),
    TrangThai nvarchar(30),
    foreign key (MaDatPhong) references dat_phong(MaDatPhong)
);
go

-- function
create function fn_tinhSoNgayThue
(
    @NgayNhan datetime,
    @NgayTra datetime
)
returns int
as
begin
    return datediff(day, @NgayNhan, @NgayTra);
end;
go

-- trigger tinh thanh tien
create trigger trg_tinhThanhTien
on chi_tiet_dat_phong
after insert, update
as
begin
    update chi_tiet_dat_phong
    set ThanhTien = DonGia * SoNgay
    where MaCTDP in (select MaCTDP from inserted);
end;
go

-- trigger cap nhat trang thai phong
create trigger trg_capNhatTrangThaiPhong
on dat_phong
after update
as
begin
    if update(TrangThai)
    begin
        update phong
        set TrangThai = N'đang thuê'
        from phong p
        join chi_tiet_dat_phong ct on p.MaPhong = ct.MaPhong
        join inserted i on ct.MaDatPhong = i.MaDatPhong
        where i.TrangThai = N'đã nhận phòng';
    end
end;
go

-- sp dat phong
create procedure sp_datPhong
    @MaKH int,
    @MaNV int,
    @NgayNhan datetime,
    @NgayTra datetime
as
begin
    insert into dat_phong(MaKH, MaNV, NgayNhan, NgayTra, TrangThai)
    values (@MaKH, @MaNV, @NgayNhan, @NgayTra, N'đã đặt');
end;
go

-- sp them chi tiet dat phong
create procedure sp_themChiTietDatPhong
    @MaDatPhong int,
    @MaPhong int,
    @DonGia decimal(18,2)
as
begin
    declare @SoNgay int;

    select @SoNgay = dbo.fn_tinhSoNgayThue(NgayNhan, NgayTra)
    from dat_phong
    where MaDatPhong = @MaDatPhong;

    insert into chi_tiet_dat_phong(MaDatPhong, MaPhong, DonGia, SoNgay)
    values (@MaDatPhong, @MaPhong, @DonGia, @SoNgay);
end;
go

-- sp tra phong
create procedure sp_traPhong
    @MaDatPhong int
as
begin
    update dat_phong
    set TrangThai = N'đã trả phòng'
    where MaDatPhong = @MaDatPhong;

    update phong
    set TrangThai = N'trống'
    where MaPhong in (
        select MaPhong
        from chi_tiet_dat_phong
        where MaDatPhong = @MaDatPhong
    );
end;
go

-- sp phong trong
create procedure sp_phongTrong
as
begin
    select *
    from phong
    where TrangThai = N'trống';
end;
go

-- sp tinh doanh thu theo thang
create procedure sp_tinhDoanhThuTheoThang
    @Thang int,
    @Nam int
as
begin
    select sum(SoTien) as TongDoanhThu
    from thanh_toan
    where month(NgayThanhToan) = @Thang
      and year(NgayThanhToan) = @Nam;
end;
go

-- sp bao cao doanh thu dung cursor
create procedure sp_baoCaoDoanhThu_cursor
as
begin
    declare @MaDatPhong int, @TongTien decimal(18,2);

    declare cur_doanhthu cursor for
    select MaDatPhong, sum(ThanhTien)
    from chi_tiet_dat_phong
    group by MaDatPhong;

    open cur_doanhthu;
    fetch next from cur_doanhthu into @MaDatPhong, @TongTien;

    while @@fetch_status = 0
    begin
        print N'đơn đặt phòng: ' + cast(@MaDatPhong as nvarchar)
            + N' - tổng tiền: ' + cast(@TongTien as nvarchar);

        fetch next from cur_doanhthu into @MaDatPhong, @TongTien;
    end;

    close cur_doanhthu;
    deallocate cur_doanhthu;
end;
go
