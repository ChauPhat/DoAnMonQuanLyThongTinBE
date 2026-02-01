/*
  Import/Export demo (SQL Server)

  IMPORT: BULK INSERT from CSV into staging tables, then MERGE into main tables.
  EXPORT: typical way is using bcp/sqlcmd (external tool). This file provides
          safe SELECT views for export + example commands.

  NOTE:
  - BULK INSERT requires SQL Server service account access to the file path.
  - For local dev, put CSV in a folder accessible by SQL Server.
*/

use ql_dat_phong;
go

-- ===== Export Views (clean schema for CSV/Excel) =====
create or alter view dbo.v_export_khach_hang
as
select MaKH, HoTen, CCCD, DienThoai, Email, DiaChi
from dbo.khach_hang;
go

create or alter view dbo.v_export_phong
as
select p.MaPhong, p.MaLoaiPhong, p.TenPhong, p.Tang, p.TrangThai
from dbo.phong p;
go

-- ===== Import staging tables =====
if object_id('dbo.stg_khach_hang', 'U') is not null drop table dbo.stg_khach_hang;
go
create table dbo.stg_khach_hang (
    HoTen nvarchar(100) not null,
    CCCD varchar(20) null,
    DienThoai varchar(15) null,
    Email varchar(100) null,
    DiaChi nvarchar(255) null
);
go

-- ===== BULK INSERT template =====
-- CSV must be UTF-8 and use comma delimiter.
-- Example file: C:\data\khach_hang.csv
-- First row is header -> FIRSTROW = 2

-- truncate table dbo.stg_khach_hang;
-- bulk insert dbo.stg_khach_hang
-- from 'C:\\data\\khach_hang.csv'
-- with (
--     firstrow = 2,
--     fieldterminator = ',',
--     rowterminator = '0x0a',
--     codepage = '65001',
--     tablock
-- );
-- go

-- ===== Merge staging into main table =====
-- Insert new customers by unique CCCD (or by HoTen+Phone if CCCD null)
create or alter procedure dbo.sp_import_khach_hang_from_staging
as
begin
    set nocount on;

    insert into dbo.khach_hang(HoTen, CCCD, DienThoai, Email, DiaChi)
    select s.HoTen, s.CCCD, s.DienThoai, s.Email, s.DiaChi
    from dbo.stg_khach_hang s
    where not exists (
        select 1
        from dbo.khach_hang k
        where (s.CCCD is not null and k.CCCD = s.CCCD)
           or (s.CCCD is null and k.HoTen = s.HoTen and isnull(k.DienThoai,'') = isnull(s.DienThoai,''))
    );
end;
go

-- ===== Export commands (run in terminal) =====
/*
  Using bcp (recommended for demo):

  bcp "select * from ql_dat_phong.dbo.v_export_khach_hang" queryout "C:\\data\\khach_hang_export.csv" -c -t, -r\n -S localhost -U sa -P root
  bcp "select * from ql_dat_phong.dbo.v_export_phong" queryout "C:\\data\\phong_export.csv" -c -t, -r\n -S localhost -U sa -P root
*/
