# ql-dat-phong-api (Spring Boot + SQL Server)

## Yêu cầu
- Java 17+
- SQL Server (đã tạo DB `ql_dat_phong` theo script bạn cung cấp)

## Cấu hình
Project đọc cấu hình từ biến môi trường (xem `src/main/resources/application.yml`):
- `DB_URL` (mặc định: `jdbc:sqlserver://localhost:1433;databaseName=ql_dat_phong;encrypt=true;trustServerCertificate=true`)
- `DB_USERNAME`
- `DB_PASSWORD`
- `APP_PORT` (mặc định `8080`)
- `CORS_ALLOWED_ORIGIN_PATTERNS` (mặc định `*`, có thể truyền nhiều giá trị cách nhau bằng dấu phẩy)

## Chạy
Project đã có Gradle Wrapper, nên bạn không cần cài Gradle.

- Chạy app: `./gradlew.bat bootRun`
- Chạy test: `./gradlew.bat test`

Nếu bạn dùng IntelliJ: có thể chạy Gradle task `bootRun`.

### Chạy SQL Server bằng Docker (tuỳ chọn)
- `docker compose --env-file .env up -d`
- Sau đó chạy app với `DB_URL/DB_USERNAME/DB_PASSWORD` như trong `.env`.

Swagger UI:
- `http://localhost:8080/swagger`

## Demo flow (CRUD danh mục + Stored Procedure/Trigger/Function/Cursor)

### CRUD danh mục (REST)
Các API danh mục để chuẩn bị dữ liệu demo:
- `GET/POST/PUT/DELETE /api/khach-hang`
- `GET/POST/PUT/DELETE /api/nhan-vien`
- `GET/POST/PUT/DELETE /api/loai-phong`
- `GET/POST/PUT/DELETE /api/phong`

### Nghiệp vụ (Stored Procedure)
Các API dưới đây gọi thẳng Stored Procedure trong SQL Server:
- `GET /api/proc/phong-trong` → `sp_phongTrong`
- `POST /api/proc/dat-phong` → `sp_datPhong` (trả về `MaDatPhong`)
- `POST /api/proc/them-chi-tiet` → `sp_themChiTietDatPhong` (tự tính `SoNgay` qua `fn_tinhSoNgayThue`)
- `POST /api/proc/tra-phong` → `sp_traPhong`
- `POST /api/proc/doanh-thu` → `sp_tinhDoanhThuTheoThang`

Lưu ý: nếu bạn đã chạy version script cũ, hãy chạy lại [database/ql_dat_phong.sql](database/ql_dat_phong.sql) (hoặc `ALTER PROCEDURE sp_datPhong`) để có output `MaDatPhong`.

Gợi ý trình tự demo nhanh:
1) Tạo `nhan_vien`, `khach_hang`, `loai_phong`, `phong` (hoặc dùng dữ liệu mẫu trong script).
2) Gọi `GET /api/proc/phong-trong` để lấy phòng trống.
3) Gọi `POST /api/proc/dat-phong` để tạo đặt phòng (nhận `MaDatPhong`).
4) Gọi `POST /api/proc/them-chi-tiet` để gắn phòng vào đặt phòng.
5) Gọi `POST /api/proc/tra-phong` để trả phòng.

### Trigger / Function / Cursor (trong script DB)
Các đối tượng DB có sẵn trong script:
- Function: `fn_tinhSoNgayThue`
- Triggers: `trg_tinhThanhTien`, `trg_capNhatTrangThaiPhong`
- Cursor procedure: `sp_baoCaoDoanhThu_cursor` (chạy trong SSMS/Azure Data Studio để xem output `PRINT`)

## Lưu ý quan trọng về DB script
Script DB đã được lưu lại để tiện chạy lại (kèm dữ liệu mẫu): [database/ql_dat_phong.sql](database/ql_dat_phong.sql)

Lưu ý: file SQL này có dùng `GO` để tách batch, nên hãy chạy bằng SSMS / Azure Data Studio (hoặc tool nào hỗ trợ `GO`).

Trong script, cột `dat_phong.TrangThai` chỉ cho phép: `đã đặt`, `đã nhận phòng`, `hủy`.
Hiện tại CHECK constraint đã bao gồm cả `đã trả phòng` (khớp với `sp_traPhong`).