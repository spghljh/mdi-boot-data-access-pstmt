package kr.co.mdi.cpu.dao.postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import kr.co.mdi.common.jdbc.AbstractJdbcDao;
import kr.co.mdi.cpu.dao.CpuDao;
import kr.co.mdi.cpu.dto.CpuDTO;

@Profile("dev-user-postgre")
@Repository
public class CpuDaoImpl extends AbstractJdbcDao implements CpuDao {

    @Override
    public List<CpuDTO> selectAllCpus() {
        List<CpuDTO> list = new ArrayList<>();
        String sql = "SELECT id_cpu, name_cpu, release_cpu, core_cpu, thread_cpu, maxghz_cpu, minghz_cpu, type_code_cpu, manf_code_cpu FROM cpu";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                CpuDTO cpu = new CpuDTO();
                cpu.setIdCpu(rs.getLong("id_cpu"));
                cpu.setNameCpu(rs.getString("name_cpu"));
                cpu.setReleaseCpu(rs.getInt("release_cpu"));
                cpu.setCoreCpu(rs.getInt("core_cpu"));
                cpu.setThreadCpu(rs.getInt("thread_cpu"));
                cpu.setMaxGhzCpu(rs.getFloat("maxghz_cpu"));
                cpu.setMinGhzCpu(rs.getFloat("minghz_cpu"));
                cpu.setTypeCodeCpu(rs.getString("type_code_cpu"));
                cpu.setManfCodeCpu(rs.getString("manf_code_cpu"));
                list.add(cpu);
            }
        } catch (Exception e) {
            throw new RuntimeException("selectAllCpus error", e);
        }
        return list;
    }

    @Override
    public CpuDTO selectCpuById(Long id) {
        String sql = "SELECT id_cpu, name_cpu, release_cpu, core_cpu, thread_cpu, maxghz_cpu, minghz_cpu, type_code_cpu, manf_code_cpu FROM cpu WHERE id_cpu = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    CpuDTO cpu = new CpuDTO();
                    cpu.setIdCpu(rs.getLong("id_cpu"));
                    cpu.setNameCpu(rs.getString("name_cpu"));
                    cpu.setReleaseCpu(rs.getInt("release_cpu"));
                    cpu.setCoreCpu(rs.getInt("core_cpu"));
                    cpu.setThreadCpu(rs.getInt("thread_cpu"));
                    cpu.setMaxGhzCpu(rs.getFloat("maxghz_cpu"));
                    cpu.setMinGhzCpu(rs.getFloat("minghz_cpu"));
                    cpu.setTypeCodeCpu(rs.getString("type_code_cpu"));
                    cpu.setManfCodeCpu(rs.getString("manf_code_cpu"));
                    return cpu;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("selectCpuById error", e);
        }
        return null;
    }

    @Override
    public CpuDTO insertCpu(CpuDTO cpu) {
        // PostgreSQL은 RETURNING 구문을 사용하여 PK를 바로 가져옴
        String sql = "INSERT INTO cpu (name_cpu, release_cpu, core_cpu, thread_cpu, maxghz_cpu, minghz_cpu, type_code_cpu, manf_code_cpu) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_cpu";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cpu.getNameCpu());
            pstmt.setInt(2, cpu.getReleaseCpu());
            pstmt.setInt(3, cpu.getCoreCpu());
            pstmt.setInt(4, cpu.getThreadCpu());
            pstmt.setFloat(5, cpu.getMaxGhzCpu());
            pstmt.setFloat(6, cpu.getMinGhzCpu());
            pstmt.setString(7, cpu.getTypeCodeCpu());
            pstmt.setString(8, cpu.getManfCodeCpu());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cpu.setIdCpu(rs.getLong("id_cpu"));
                }
            }
            return cpu;
        } catch (Exception e) {
            throw new RuntimeException("insertCpu error", e);
        }
    }

    @Override
    public CpuDTO updateCpu(CpuDTO cpu) {
        String sql = "UPDATE cpu SET name_cpu=?, release_cpu=?, core_cpu=?, thread_cpu=?, maxghz_cpu=?, minghz_cpu=?, type_code_cpu=?, manf_code_cpu=? WHERE id_cpu=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cpu.getNameCpu());
            pstmt.setInt(2, cpu.getReleaseCpu());
            pstmt.setInt(3, cpu.getCoreCpu());
            pstmt.setInt(4, cpu.getThreadCpu());
            pstmt.setFloat(5, cpu.getMaxGhzCpu());
            pstmt.setFloat(6, cpu.getMinGhzCpu());
            pstmt.setString(7, cpu.getTypeCodeCpu());
            pstmt.setString(8, cpu.getManfCodeCpu());
            pstmt.setLong(9, cpu.getIdCpu());
            pstmt.executeUpdate();
            return cpu;
        } catch (Exception e) {
            throw new RuntimeException("updateCpu error", e);
        }
    }

    @Override
    public void deleteCpu(Long id) {
        String sql = "DELETE FROM cpu WHERE id_cpu=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("deleteCpu error", e);
        }
    }
}
