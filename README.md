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

## Lưu ý quan trọng về DB script
Script DB đã được lưu lại để tiện chạy lại: [database/ql_dat_phong.sql](database/ql_dat_phong.sql)

Trong script, cột `dat_phong.TrangThai` chỉ cho phép: `đã đặt`, `đã nhận phòng`, `hủy`.
Nhưng stored procedure `sp_traPhong` lại cập nhật `TrangThai = 'đã trả phòng'` nên có thể bị lỗi CHECK constraint khi gọi.
Bạn cần sửa lại CHECK constraint hoặc sửa procedure để khớp.