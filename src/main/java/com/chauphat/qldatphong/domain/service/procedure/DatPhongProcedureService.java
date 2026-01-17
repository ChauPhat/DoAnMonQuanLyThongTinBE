package com.chauphat.qldatphong.domain.service.procedure;

import com.chauphat.qldatphong.domain.entity.Phong;
import com.chauphat.qldatphong.domain.entity.LoaiPhong;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.CallableStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatPhongProcedureService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public Integer datPhong(Integer maKh, Integer maNv, LocalDateTime ngayNhan, LocalDateTime ngayTra) {
        return jdbcTemplate.execute(
                (CallableStatementCreator) con -> {
                    CallableStatement cs = con.prepareCall("{call dbo.sp_datPhong(?, ?, ?, ?, ?)}");
                    cs.setInt(1, maKh);
                    cs.setInt(2, maNv);
                    cs.setTimestamp(3, Timestamp.valueOf(ngayNhan));
                    cs.setTimestamp(4, Timestamp.valueOf(ngayTra));
                    cs.registerOutParameter(5, Types.INTEGER);
                    return cs;
                },
                (CallableStatementCallback<Integer>) cs -> {
                    cs.execute();
                    int id = cs.getInt(5);
                    return cs.wasNull() ? null : id;
                }
        );
    }

    public void themChiTietDatPhong(Integer maDatPhong, Integer maPhong, BigDecimal donGia) {
        SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
                .withSchemaName("dbo")
            .withProcedureName("sp_themChiTietDatPhong")
            .withoutProcedureColumnMetaDataAccess()
            .declareParameters(
                new SqlParameter("MaDatPhong", Types.INTEGER),
                new SqlParameter("MaPhong", Types.INTEGER),
                new SqlParameter("DonGia", Types.DECIMAL)
            );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("MaDatPhong", maDatPhong)
                .addValue("MaPhong", maPhong)
                .addValue("DonGia", donGia);

        call.execute(params);
    }

    public void traPhong(Integer maDatPhong) {
        SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
                .withSchemaName("dbo")
                .withProcedureName("sp_traPhong")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("MaDatPhong", Types.INTEGER));

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("MaDatPhong", maDatPhong);

        call.execute(params);
    }

    @SuppressWarnings("unchecked")
    public List<Phong> phongTrong() {
        SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
                .withSchemaName("dbo")
                .withProcedureName("sp_phongTrong")
                .withoutProcedureColumnMetaDataAccess()
                .returningResultSet("result", new PhongRowMapper());

        Map<String, Object> out = call.execute(new MapSqlParameterSource());
        return (List<Phong>) out.get("result");
    }

        @SuppressWarnings("unchecked")
        public List<Phong> phongDaDat(LocalDateTime tuNgay, LocalDateTime denNgay) {
        SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
            .withSchemaName("dbo")
            .withProcedureName("sp_phongDaDatTheoKhoangThoiGian")
            .withoutProcedureColumnMetaDataAccess()
            .declareParameters(
                new SqlParameter("TuNgay", Types.TIMESTAMP),
                new SqlParameter("DenNgay", Types.TIMESTAMP)
            )
            .returningResultSet("result", new PhongRowMapper());

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("TuNgay", Timestamp.valueOf(tuNgay))
            .addValue("DenNgay", Timestamp.valueOf(denNgay));

        Map<String, Object> out = call.execute(params);
        return (List<Phong>) out.get("result");
        }

    public BigDecimal tinhDoanhThuTheoThang(int thang, int nam) {
        BigDecimal value = jdbcTemplate.queryForObject(
                "exec dbo.sp_tinhDoanhThuTheoThang ?, ?",
                BigDecimal.class,
                thang,
                nam
        );
        return value != null ? value : BigDecimal.ZERO;
    }

    private static class PhongRowMapper implements RowMapper<Phong> {
        @Override
        public Phong mapRow(ResultSet rs, int rowNum) throws SQLException {
            Phong p = new Phong();
            p.setMaPhong(rs.getInt("MaPhong"));
            Integer maLoaiPhong = (Integer) rs.getObject("MaLoaiPhong");
            if (maLoaiPhong != null) {
                p.setLoaiPhong(LoaiPhong.builder().maLoaiPhong(maLoaiPhong).build());
            }
            p.setTenPhong(rs.getString("TenPhong"));
            p.setTang((Integer) rs.getObject("Tang"));
            p.setTrangThai(rs.getString("TrangThai"));
            return p;
        }
    }
}
