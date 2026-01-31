package kr.co.mdi.device.dao.oracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import kr.co.mdi.common.jdbc.AbstractJdbcDao;
import kr.co.mdi.device.dao.DeviceDao;
import kr.co.mdi.device.dto.DeviceDTO;

@Profile("dev-user-oracle")
@Repository
public class DeviceDaoImpl extends AbstractJdbcDao implements DeviceDao {

    @Override
    public List<DeviceDTO> selectAllDevices() {
        List<DeviceDTO> list = new ArrayList<>();
        String sql = "SELECT id_device, name_device, id_cpu, lineup_device, release_device, weight_device, type_code_device, manf_code_device FROM device";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                DeviceDTO device = new DeviceDTO();
                device.setIdDevice(rs.getLong("id_device"));
                device.setNameDevice(rs.getString("name_device"));
                device.setIdCpu(rs.getLong("id_cpu"));
                device.setLineupDevice(rs.getString("lineup_device"));
                device.setReleaseDevice(rs.getInt("release_device"));
                device.setWeightDevice(rs.getFloat("weight_device"));
                device.setTypeCodeDevice(rs.getString("type_code_device"));
                device.setManfCodeDevice(rs.getString("manf_code_device"));
                list.add(device);
            }
        } catch (Exception e) {
            throw new RuntimeException("selectAllDevices error", e);
        }
        return list;
    }

    @Override
    public DeviceDTO selectDeviceById(Long id) {
        String sql = "SELECT id_device, name_device, id_cpu, lineup_device, release_device, weight_device, type_code_device, manf_code_device FROM device WHERE id_device=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DeviceDTO device = new DeviceDTO();
                    device.setIdDevice(rs.getLong("id_device"));
                    device.setNameDevice(rs.getString("name_device"));
                    device.setIdCpu(rs.getLong("id_cpu"));
                    device.setLineupDevice(rs.getString("lineup_device"));
                    device.setReleaseDevice(rs.getInt("release_device"));
                    device.setWeightDevice(rs.getFloat("weight_device"));
                    device.setTypeCodeDevice(rs.getString("type_code_device"));
                    device.setManfCodeDevice(rs.getString("manf_code_device"));
                    return device;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("selectDeviceById error", e);
        }
        return null;
    }

    @Override
    public DeviceDTO insertDevice(DeviceDTO device) {
        // Oracle은 시퀀스 사용 → device_seq.NEXTVAL
        String sql = "INSERT INTO device (id_device, name_device, id_cpu, lineup_device, release_device, weight_device, type_code_device, manf_code_device) " +
                     "VALUES (device_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"id_device"})) {
            pstmt.setString(1, device.getNameDevice());
            pstmt.setLong(2, device.getIdCpu());
            pstmt.setString(3, device.getLineupDevice());
            pstmt.setInt(4, device.getReleaseDevice());
            pstmt.setFloat(5, device.getWeightDevice());
            pstmt.setString(6, device.getTypeCodeDevice());
            pstmt.setString(7, device.getManfCodeDevice());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    device.setIdDevice(rs.getLong(1));
                }
            }
            return device;
        } catch (Exception e) {
            throw new RuntimeException("insertDevice error", e);
        }
    }

    @Override
    public DeviceDTO updateDevice(DeviceDTO device) {
        String sql = "UPDATE device SET name_device=?, id_cpu=?, lineup_device=?, release_device=?, weight_device=?, type_code_device=?, manf_code_device=? WHERE id_device=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, device.getNameDevice());
            pstmt.setLong(2, device.getIdCpu());
            pstmt.setString(3, device.getLineupDevice());
            pstmt.setInt(4, device.getReleaseDevice());
            pstmt.setFloat(5, device.getWeightDevice());
            pstmt.setString(6, device.getTypeCodeDevice());
            pstmt.setString(7, device.getManfCodeDevice());
            pstmt.setLong(8, device.getIdDevice());
            pstmt.executeUpdate();
            return device;
        } catch (Exception e) {
            throw new RuntimeException("updateDevice error", e);
        }
    }

    @Override
    public void deleteDevice(Long id) {
        String sql = "DELETE FROM device WHERE id_device=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("deleteDevice error", e);
        }
    }
}
