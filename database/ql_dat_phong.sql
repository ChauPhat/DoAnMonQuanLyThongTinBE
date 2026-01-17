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
    TenNV nvarchar(100) not null,
    VaiTro nvarchar(50) not null,
    Username varchar(50) not null unique,
    Password varchar(255) not null
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
        check (TrangThai in (N'đã đặt', N'đã nhận phòng', N'hủy', N'đã trả phòng')),
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
    MaDatPhong int not null unique,
    NgayThanhToan datetime,
    SoTien decimal(18,2),
    PhuongThuc nvarchar(50),
    TrangThai nvarchar(30),
    foreign key (MaDatPhong) references dat_phong(MaDatPhong)
);
go

-- insert bang khach_hang
insert into khach_hang (HoTen, CCCD, DienThoai, Email, DiaChi)
values
(N'Nguyễn Văn An', '012345678901', '0901234567', 'an@gmail.com', N'Hà Nội'),
(N'Trần Thị Bình', '012345678902', '0912345678', 'binh@gmail.com', N'Đà Nẵng'),
(N'Lê Văn Cường', '012345678903', '0923456789', 'cuong@gmail.com', N'TP HCM');

-- insert bang nhan_vien
insert into nhan_vien (TenNV, VaiTro, Username, Password)
values
(N'Phạm Thị Lan', N'Lễ tân', 'lanpt', '123456'),
(N'Nguyễn Văn Hùng', N'Quản lý', 'hungnv', '123456');

-- insert bang loai_phong 
insert into loai_phong (TenLoaiPhong, SucChua, GiaCoBan, MoTa)
values
(N'Phòng đơn', 1, 500000, N'Phòng dành cho 1 người'),
(N'Phòng đôi', 2, 800000, N'Phòng dành cho 2 người'),
(N'Phòng hội nghị', 50, 3000000, N'Phòng hội nghị lớn');

--  insert bang phong 
insert into phong (MaLoaiPhong, TenPhong, Tang, TrangThai)
values
(1, N'P101', 1, N'trống'),
(1, N'P102', 1, N'trống'),
(2, N'P201', 2, N'trống'),
(2, N'P202', 2, N'trống'),
(3, N'HN01', 3, N'trống');

-- insert bang dat_phong
insert into dat_phong (MaKH, MaNV, NgayNhan, NgayTra, TrangThai)
values
(1, 1, '2026-01-20', '2026-01-22', N'đã đặt'),
(2, 1, '2026-01-21', '2026-01-23', N'đã đặt');

-- insert bang chi_tiet_dat_phong
insert into chi_tiet_dat_phong (MaDatPhong, MaPhong, DonGia, SoNgay)
values
(1, 1, 500000, 2),
(1, 2, 500000, 2),
(2, 3, 800000, 2);

-- insert bang thanh_toan
insert into thanh_toan (MaDatPhong, NgayThanhToan, SoTien, PhuongThuc, TrangThai)
values
(1, '2026-01-22', 2000000, N'Tiền mặt', N'Đã thanh toán'),
(2, '2026-01-23', 1600000, N'Chuyển khoản', N'Đã thanh toán');

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
        update p
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
        @NgayTra datetime,
        @MaDatPhong int output
as
begin
    insert into dat_phong(MaKH, MaNV, NgayNhan, NgayTra, TrangThai)
    values (@MaKH, @MaNV, @NgayNhan, @NgayTra, N'đã đặt');
        set @MaDatPhong = SCOPE_IDENTITY(); -- Capture the newly created MaDatPhong
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

    if (@SoNgay is null or @SoNgay < 1)
        set @SoNgay = 1;

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
