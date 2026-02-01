/*
  Backup/Restore demo commands for SQL Server.
  Run as sysadmin (sa) in SSMS.

  TIP: Use absolute paths that SQL Server service account can access.
*/

-- ===== BACKUP =====
-- Example path: C:\sqlbackup\ql_dat_phong_20260201.bak
backup database ql_dat_phong
to disk = 'C:\\sqlbackup\\ql_dat_phong_20260201.bak'
with init, compression, stats = 10;
go

-- ===== RESTORE =====
-- Restore requires forcing users off the DB.
-- WARNING: This will replace the existing database.

-- alter database ql_dat_phong set single_user with rollback immediate;
-- go
-- restore database ql_dat_phong
-- from disk = 'C:\\sqlbackup\\ql_dat_phong_20260201.bak'
-- with replace, stats = 10;
-- go
-- alter database ql_dat_phong set multi_user;
-- go
