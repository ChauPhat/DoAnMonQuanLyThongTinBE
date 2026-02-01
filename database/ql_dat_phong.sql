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
    SucChua int not null check (SucChua > 0),
    GiaCoBan decimal(18,2) not null check (GiaCoBan > 0),
    MoTa nvarchar(255)
);
go

create table phong (
    MaPhong int identity primary key,
    MaLoaiPhong int not null,
    TenPhong nvarchar(50) not null unique,
    Tang int,
    TrangThai nvarchar(20) not null
        default N'trống'
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
    NgayDat datetime not null default getdate(),
    NgayNhan datetime not null,
    NgayTra datetime not null,
    TrangThai nvarchar(30) not null
        default N'đã đặt'
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
    DonGia decimal(18,2) not null check (DonGia > 0),
    SoNgay int not null check (SoNgay > 0),
    ThanhTien decimal(18,2),
    foreign key (MaDatPhong) references dat_phong(MaDatPhong),
    foreign key (MaPhong) references phong(MaPhong),
    constraint UQ_chi_tiet_dat_phong unique (MaDatPhong, MaPhong)
);
go

create table thanh_toan (
    MaThanhToan int identity primary key,
    MaDatPhong int not null unique,
    NgayThanhToan datetime,
    SoTien decimal(18,2) not null check (SoTien >= 0),
    PhuongThuc nvarchar(50),
    TrangThai nvarchar(30) not null
        default N'Đã thanh toán'
        check (TrangThai in (N'Đã thanh toán', N'Chưa thanh toán', N'Hủy')),
    foreign key (MaDatPhong) references dat_phong(MaDatPhong)
);
go

-- insert bang khach_hang
insert into khach_hang (HoTen, CCCD, DienThoai, Email, DiaChi)
values
(N'Nguyễn Văn An', '012345678901', '0901234567', 'an@gmail.com', N'Hà Nội'),
(N'Trần Thị Bình', '012345678902', '0912345678', 'binh@gmail.com', N'Đà Nẵng'),
(N'Lê Văn Cường', '012345678903', '0923456789', 'cuong@gmail.com', N'TP HCM'),
(N'Phạm Thị Duyên', '012345678904', '0934567890', 'duyen@gmail.com', N'Hải Phòng'),
(N'Vũ Văn Đức', '012345678905', '0945678901', 'duc@gmail.com', N'Quảng Ninh'),
(N'Đặng Thị Hạnh', '012345678906', '0956789012', 'hanh@gmail.com', N'Bắc Ninh'),
(N'Hoàng Văn Khải', '012345678907', '0967890123', 'khai@gmail.com', N'Nghệ An'),
(N'Ngô Thị Linh', '012345678908', '0978901234', 'linh@gmail.com', N'Thừa Thiên Huế'),
(N'Bùi Văn Minh', '012345678909', '0989012345', 'minh@gmail.com', N'Khánh Hòa'),
(N'Lý Thị Ngọc', '012345678910', '0909123456', 'ngoc@gmail.com', N'Cần Thơ'),
(N'Đoàn Văn Phúc', '012345678911', '0910234567', 'phuc@gmail.com', N'Bình Dương'),
(N'Tống Thị Quỳnh', '012345678912', '0921345678', 'quynh@gmail.com', N'Lâm Đồng');

-- insert bang nhan_vien
insert into nhan_vien (TenNV, VaiTro, Username, Password)
values
(N'Phạm Thị Lan', N'Lễ tân', 'lanpt', '123456'),
(N'Nguyễn Văn Hùng', N'Quản lý', 'hungnv', '123456'),
(N'Võ Thị Minh', N'Lễ tân', 'minhvt', '123456'),
(N'Lê Văn Sơn', N'Kế toán', 'sonlv', '123456'),
(N'Nguyễn Thị Trang', N'CSKH', 'trangnt', '123456'),
(N'Phan Văn Khoa', N'Kỹ thuật', 'khoapv', '123456'),
(N'Đinh Thị Mai', N'Lễ tân', 'maidt', '123456'),
(N'Trần Văn Long', N'Bảo vệ', 'longtv', '123456'),
(N'Ngô Văn Phát', N'Quản lý', 'phatnv', '123456'),
(N'Phạm Thị Yến', N'Kế toán', 'yenpt', '123456');

-- insert bang loai_phong
insert into loai_phong (TenLoaiPhong, SucChua, GiaCoBan, MoTa)
values
(N'Phòng đơn', 1, 500000, N'Phòng dành cho 1 người'),
(N'Phòng đôi', 2, 800000, N'Phòng dành cho 2 người'),
(N'Phòng hội nghị', 50, 3000000, N'Phòng hội nghị lớn'),
(N'Phòng family', 4, 1200000, N'Phòng gia đình 4 người'),
(N'Phòng suite', 2, 2000000, N'Phòng cao cấp'),
(N'Phòng dorm', 6, 1500000, N'Phòng tập thể'),
(N'Phòng deluxe', 2, 1000000, N'Phòng deluxe'),
(N'Phòng standard', 2, 700000, N'Phòng tiêu chuẩn'),
(N'Phòng studio', 2, 900000, N'Phòng studio'),
(N'Phòng VIP', 2, 2500000, N'Phòng VIP');

--  insert bang phong
insert into phong (MaLoaiPhong, TenPhong, Tang, TrangThai)
values
(1, N'P101', 1, N'trống'),
(1, N'P102', 1, N'trống'),
(1, N'P103', 1, N'trống'),
(1, N'P104', 1, N'trống'),
(1, N'P105', 1, N'trống'),
(1, N'P106', 1, N'trống'),
(7, N'P107', 1, N'trống'),
(7, N'P108', 1, N'trống'),
(7, N'P109', 1, N'trống'),
(7, N'P110', 1, N'bảo trì'),
(2, N'P201', 2, N'trống'),
(2, N'P202', 2, N'trống'),
(2, N'P203', 2, N'trống'),
(2, N'P204', 2, N'trống'),
(2, N'P205', 2, N'trống'),
(2, N'P206', 2, N'trống'),
(4, N'P207', 2, N'trống'),
(4, N'P208', 2, N'trống'),
(3, N'HN01', 3, N'trống'),
(3, N'HN02', 3, N'bảo trì');

go

-- function
create or alter function dbo.fn_tinhSoNgayThue
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

-- function: tong tien theo ma dat phong
create or alter function dbo.fn_tinhTongTienDatPhong
(
    @MaDatPhong int
)
returns decimal(18,2)
as
begin
    declare @Tong decimal(18,2);

    select @Tong = isnull(sum(isnull(ct.ThanhTien, ct.DonGia * ct.SoNgay)), 0)
    from chi_tiet_dat_phong ct
    where ct.MaDatPhong = @MaDatPhong;

    return isnull(@Tong, 0);
end;
go

-- function: kiem tra phong trong theo khoang thoi gian
create or alter function dbo.fn_isPhongTrongTheoKhoang
(
    @MaPhong int,
    @NgayNhan datetime,
    @NgayTra datetime
)
returns bit
as
begin
    declare @Ok bit = 1;

    if (@MaPhong is null or @NgayNhan is null or @NgayTra is null or @NgayTra <= @NgayNhan)
        return 0;

    if exists (select 1 from phong p where p.MaPhong = @MaPhong and p.TrangThai = N'bảo trì')
        return 0;

    if exists (
        select 1
        from chi_tiet_dat_phong ct
        join dat_phong dp on dp.MaDatPhong = ct.MaDatPhong
        where ct.MaPhong = @MaPhong
          and dp.TrangThai in (N'đã đặt', N'đã nhận phòng')
          and dp.NgayNhan < @NgayTra
          and dp.NgayTra > @NgayNhan
    )
        set @Ok = 0;

    return @Ok;
end;
go

-- trigger tinh thanh tien
create trigger trg_tinhThanhTien
on chi_tiet_dat_phong
after insert, update
as
begin
    set nocount on;
    if (update(DonGia) or update(SoNgay))
    begin
        update ct
        set ThanhTien = ct.DonGia * ct.SoNgay
        from chi_tiet_dat_phong ct
        join inserted i on ct.MaCTDP = i.MaCTDP
        where ct.DonGia is not null
          and ct.SoNgay is not null
          and (ct.ThanhTien is null or ct.ThanhTien <> ct.DonGia * ct.SoNgay);
    end
end;
go

-- trigger cap nhat trang thai phong
create trigger trg_capNhatTrangThaiPhong
on dat_phong
after update
as
begin
    set nocount on;

    if update(TrangThai)
    begin
        update p
        set TrangThai = case
            when i.TrangThai = N'đã nhận phòng' then N'đang thuê'
            when i.TrangThai in (N'đã trả phòng', N'hủy') then N'trống'
            else p.TrangThai
        end
        from phong p
        join chi_tiet_dat_phong ct on p.MaPhong = ct.MaPhong
        join inserted i on ct.MaDatPhong = i.MaDatPhong
        where i.TrangThai in (N'đã nhận phòng', N'đã trả phòng', N'hủy');
    end
end;
go

-- trigger: khi update ngay nhan/ngay tra -> cap nhat SoNgay, ThanhTien
create trigger trg_capNhatSoNgayChiTiet
on dat_phong
after update
as
begin
    set nocount on;

    if (update(NgayNhan) or update(NgayTra))
    begin
        update ct
        set
            SoNgay = case
                when dp.NgayTra > dp.NgayNhan then dbo.fn_tinhSoNgayThue(dp.NgayNhan, dp.NgayTra)
                else ct.SoNgay
            end,
            ThanhTien = ct.DonGia * case
                when dp.NgayTra > dp.NgayNhan then dbo.fn_tinhSoNgayThue(dp.NgayNhan, dp.NgayTra)
                else ct.SoNgay
            end
        from chi_tiet_dat_phong ct
        join inserted i on i.MaDatPhong = ct.MaDatPhong
        join dat_phong dp on dp.MaDatPhong = i.MaDatPhong;
    end
end;
go

-- trigger: chan dat trung phong theo khoang thoi gian khi chen/sua chi tiet
create trigger trg_chanDatTrungPhong
on chi_tiet_dat_phong
after insert, update
as
begin
    set nocount on;

    if exists (
        select 1
        from inserted i
        join dat_phong dpNew on dpNew.MaDatPhong = i.MaDatPhong
        join chi_tiet_dat_phong ct on ct.MaPhong = i.MaPhong
        join dat_phong dp on dp.MaDatPhong = ct.MaDatPhong
        where dp.TrangThai in (N'đã đặt', N'đã nhận phòng')
          and dpNew.TrangThai in (N'đã đặt', N'đã nhận phòng')
          and dp.MaDatPhong <> dpNew.MaDatPhong
          and dp.NgayNhan < dpNew.NgayTra
          and dp.NgayTra > dpNew.NgayNhan
    )
    begin
        raiserror(N'Phòng đã được đặt trong khoảng thời gian này', 16, 1);
        rollback tran;
        return;
    end
end;
go

-- trigger: tu dong set NgayThanhToan khi da thanh toan
create trigger trg_setNgayThanhToan
on thanh_toan
after insert, update
as
begin
    set nocount on;

    update tt
    set NgayThanhToan = isnull(tt.NgayThanhToan, getdate())
    from thanh_toan tt
    join inserted i on i.MaThanhToan = tt.MaThanhToan
    where tt.TrangThai = N'Đã thanh toán'
      and tt.NgayThanhToan is null;
end;
go


-- sp dat phong nhanh (tạo đặt phòng + tự thêm chi tiết theo danh sách phòng)
create or alter procedure dbo.sp_datPhongNhanh
    @MaKH int,
    @MaNV int,
    @NgayNhan datetime,
    @NgayTra datetime,
    @DanhSachMaPhong nvarchar(max),
    @MaDatPhong int output
as
begin
    set nocount on;

    if (@NgayTra is null or @NgayNhan is null or @NgayTra <= @NgayNhan)
    begin
        raiserror(N'Khoảng ngày nhận/trả không hợp lệ', 16, 1);
        return;
    end

    if not exists (select 1 from khach_hang where MaKH = @MaKH)
    begin
        raiserror(N'MaKH không tồn tại', 16, 1);
        return;
    end

    if not exists (select 1 from nhan_vien where MaNV = @MaNV)
    begin
        raiserror(N'MaNV không tồn tại', 16, 1);
        return;
    end

    if (@DanhSachMaPhong is null or ltrim(rtrim(@DanhSachMaPhong)) = N'')
    begin
        raiserror(N'Danh sách phòng không được rỗng', 16, 1);
        return;
    end

    declare @SoNgay int = dbo.fn_tinhSoNgayThue(@NgayNhan, @NgayTra);
    if (@SoNgay is null or @SoNgay < 1)
        set @SoNgay = 1;

    begin try
        begin tran;

        declare @RoomTokens table (
            Token nvarchar(50) not null,
            MaPhong int null
        );

        insert into @RoomTokens(Token, MaPhong)
        select
            ltrim(rtrim(value)) as Token,
            try_cast(ltrim(rtrim(value)) as int) as MaPhong
        from string_split(@DanhSachMaPhong, ',')
        where ltrim(rtrim(value)) <> '';

        if not exists (select 1 from @RoomTokens)
        begin
            raiserror(N'Danh sách phòng không được rỗng', 16, 1);
            rollback tran;
            return;
        end
        if exists (select 1 from @RoomTokens where MaPhong is null)
        begin
            raiserror(N'Danh sách phòng không hợp lệ', 16, 1);
            rollback tran;
            return;
        end

        declare @Rooms table (MaPhong int primary key);
        insert into @Rooms(MaPhong)
        select distinct MaPhong
        from @RoomTokens;

        if not exists (select 1 from @Rooms)
        begin
            raiserror(N'Danh sách phòng không được rỗng', 16, 1);
            rollback tran;
            return;
        end

        if exists (
            select 1
            from @Rooms r
            left join phong p on p.MaPhong = r.MaPhong
            where p.MaPhong is null
        )
        begin
            raiserror(N'Có phòng không tồn tại', 16, 1);
            rollback tran;
            return;
        end

        if exists (
            select 1
            from @Rooms r
            join phong p on p.MaPhong = r.MaPhong
            where p.TrangThai = N'bảo trì'
        )
        begin
            raiserror(N'Có phòng đang bảo trì', 16, 1);
            rollback tran;
            return;
        end

        if exists (
            select 1
                        from @Rooms r
            join chi_tiet_dat_phong ct on ct.MaPhong = r.MaPhong
            join dat_phong dp on dp.MaDatPhong = ct.MaDatPhong
            where dp.TrangThai in (N'đã đặt', N'đã nhận phòng')
              and dp.NgayNhan < @NgayTra
              and dp.NgayTra > @NgayNhan
        )
        begin
            raiserror(N'Có phòng đã được đặt trong khoảng thời gian này', 16, 1);
            rollback tran;
            return;
        end

        insert into dat_phong(MaKH, MaNV, NgayNhan, NgayTra, TrangThai)
        values (@MaKH, @MaNV, @NgayNhan, @NgayTra, N'đã đặt');

        set @MaDatPhong = cast(scope_identity() as int);

        insert into chi_tiet_dat_phong(MaDatPhong, MaPhong, DonGia, SoNgay)
        select
            @MaDatPhong,
            p.MaPhong,
            lp.GiaCoBan,
            @SoNgay
        from @Rooms r
        join phong p on p.MaPhong = r.MaPhong
        join loai_phong lp on lp.MaLoaiPhong = p.MaLoaiPhong;

        commit tran;
    end try
    begin catch
        if @@trancount > 0
            rollback tran;
        throw;
    end catch
end;
go

-- sp nhan phong (check-in)
create or alter procedure dbo.sp_nhanPhong
    @MaDatPhong int
as
begin
    set nocount on;

    if not exists (select 1 from dat_phong where MaDatPhong = @MaDatPhong)
    begin
        raiserror(N'MaDatPhong không tồn tại', 16, 1);
        return;
    end

    if exists (
        select 1
        from dat_phong
        where MaDatPhong = @MaDatPhong
          and TrangThai in (N'hủy', N'đã trả phòng')
    )
    begin
        raiserror(N'Đặt phòng không hợp lệ để nhận phòng', 16, 1);
        return;
    end

    if not exists (select 1 from chi_tiet_dat_phong where MaDatPhong = @MaDatPhong)
    begin
        raiserror(N'Đặt phòng chưa có chi tiết phòng', 16, 1);
        return;
    end

    if exists (
        select 1
        from chi_tiet_dat_phong ct
        join phong p on p.MaPhong = ct.MaPhong
        where ct.MaDatPhong = @MaDatPhong
          and p.TrangThai <> N'trống'
    )
    begin
        raiserror(N'Có phòng không ở trạng thái trống', 16, 1);
        return;
    end

    update dat_phong
    set TrangThai = N'đã nhận phòng'
    where MaDatPhong = @MaDatPhong
      and TrangThai = N'đã đặt';

    if @@rowcount = 0
    begin
        raiserror(N'Chỉ có thể nhận phòng khi trạng thái là "đã đặt"', 16, 1);
        return;
    end
end;
go

-- sp tra phong
create or alter procedure dbo.sp_traPhong
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
create or alter procedure dbo.sp_phongTrong
as
begin
    select *
    from phong
    where TrangThai = N'trống';
end;
go

-- sp phong da dat theo khoang thoi gian
create or alter procedure dbo.sp_phongDaDatTheoKhoangThoiGian
    @TuNgay datetime,
    @DenNgay datetime
as
begin
    set nocount on;
    select distinct p.*
    from phong p
    join chi_tiet_dat_phong ct on p.MaPhong = ct.MaPhong
    join dat_phong dp on dp.MaDatPhong = ct.MaDatPhong
    where dp.TrangThai in (N'đã đặt', N'đã nhận phòng')
      and dp.NgayNhan < @DenNgay
      and dp.NgayTra > @TuNgay;
end;
go

-- sp tinh doanh thu theo thang
create or alter procedure dbo.sp_tinhDoanhThuTheoThang
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
create or alter procedure dbo.sp_baoCaoDoanhThu_cursor
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

-- sp bao cao doanh thu dung cursor (RETURN result set cho FE)
create or alter procedure dbo.sp_baoCaoDoanhThu_cursor_report
as
begin
    set nocount on;

    declare
        @MaDatPhong int,
        @MaKH int,
        @HoTenKH nvarchar(100),
        @NgayDat datetime,
        @TrangThai nvarchar(30),
        @TongTien decimal(18,2);

    declare @Report table (
        MaDatPhong int not null,
        MaKH int not null,
        HoTenKH nvarchar(100) not null,
        NgayDat datetime not null,
        TrangThai nvarchar(30) not null,
        TongTien decimal(18,2) not null
    );

    declare cur_doanhthu_report cursor for
        select
            dp.MaDatPhong,
            dp.MaKH,
            kh.HoTen as HoTenKH,
            dp.NgayDat,
            dp.TrangThai,
            isnull((
                select sum(isnull(ct.ThanhTien, ct.DonGia * ct.SoNgay))
                from chi_tiet_dat_phong ct
                where ct.MaDatPhong = dp.MaDatPhong
            ), 0) as TongTien
        from dat_phong dp
        join khach_hang kh on kh.MaKH = dp.MaKH
        order by dp.MaDatPhong desc;

    open cur_doanhthu_report;
    fetch next from cur_doanhthu_report into @MaDatPhong, @MaKH, @HoTenKH, @NgayDat, @TrangThai, @TongTien;

    while @@fetch_status = 0
    begin
        insert into @Report(MaDatPhong, MaKH, HoTenKH, NgayDat, TrangThai, TongTien)
        values (@MaDatPhong, @MaKH, @HoTenKH, @NgayDat, @TrangThai, isnull(@TongTien, 0));

        fetch next from cur_doanhthu_report into @MaDatPhong, @MaKH, @HoTenKH, @NgayDat, @TrangThai, @TongTien;
    end;

    close cur_doanhthu_report;
    deallocate cur_doanhthu_report;

    select *
    from @Report
    order by MaDatPhong desc;
end;
go

-- sp danh sach dat phong (phục vụ combobox: mã gần đây + search theo mã)
create or alter procedure dbo.sp_danhSachDatPhong
    @Limit int = 20,
    @Search nvarchar(50) = null
as
begin
    set nocount on;

    if (@Limit is null or @Limit < 1) set @Limit = 20;
    if (@Limit > 100) set @Limit = 100;

    select top (@Limit)
        dp.MaDatPhong,
        dp.MaKH,
        kh.HoTen as HoTenKH,
        dp.MaNV,
        nv.TenNV,
        dp.NgayDat,
        dp.NgayNhan,
        dp.NgayTra,
        dp.TrangThai,
        (select count(1) from chi_tiet_dat_phong ct where ct.MaDatPhong = dp.MaDatPhong) as SoPhong,
        (
            select sum(isnull(ct.ThanhTien, ct.DonGia * ct.SoNgay))
            from chi_tiet_dat_phong ct
            where ct.MaDatPhong = dp.MaDatPhong
        ) as TongTien
    from dat_phong dp
    join khach_hang kh on kh.MaKH = dp.MaKH
    join nhan_vien nv on nv.MaNV = dp.MaNV
    where (@Search is null or ltrim(rtrim(@Search)) = N'')
       or cast(dp.MaDatPhong as nvarchar(50)) like N'%' + ltrim(rtrim(@Search)) + N'%'
    order by dp.MaDatPhong desc;
end;
go

-- sp thong tin dat phong theo ma (phục vụ khi user gõ mã bất kỳ)
create or alter procedure dbo.sp_thongTinDatPhong
    @MaDatPhong int
as
begin
    set nocount on;

    select
        dp.MaDatPhong,
        dp.MaKH,
        kh.HoTen as HoTenKH,
        dp.MaNV,
        nv.TenNV,
        dp.NgayDat,
        dp.NgayNhan,
        dp.NgayTra,
        dp.TrangThai,
        (select count(1) from chi_tiet_dat_phong ct where ct.MaDatPhong = dp.MaDatPhong) as SoPhong,
        (
            select sum(isnull(ct.ThanhTien, ct.DonGia * ct.SoNgay))
            from chi_tiet_dat_phong ct
            where ct.MaDatPhong = dp.MaDatPhong
        ) as TongTien
    from dat_phong dp
    join khach_hang kh on kh.MaKH = dp.MaKH
    join nhan_vien nv on nv.MaNV = dp.MaNV
    where dp.MaDatPhong = @MaDatPhong;
end;
go
