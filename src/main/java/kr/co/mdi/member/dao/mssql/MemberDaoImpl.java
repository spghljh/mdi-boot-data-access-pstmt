package kr.co.mdi.member.dao.mssql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import kr.co.mdi.common.jdbc.AbstractJdbcDao;
import kr.co.mdi.member.dao.MemberDao;
import kr.co.mdi.member.dto.MemberDTO;

@Profile("dev-user-mssql")
@Repository
public class MemberDaoImpl extends AbstractJdbcDao implements MemberDao {

    @Override
    public List<MemberDTO> selectAllMembers() {
        List<MemberDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM member";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("selectAllMembers error", e);
        }
        return list;
    }

    @Override
    public MemberDTO selectMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id_member=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("selectMemberById error", e);
        }
        return null;
    }

    @Override
    public MemberDTO insertMember(MemberDTO member) {
        String sql = "INSERT INTO member (id, pass, name, email, role, status, email_verified) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPass());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getRole());
            pstmt.setString(6, member.getStatus());
            pstmt.setString(7, member.getEmailVerified());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    member.setIdMember(rs.getLong(1));
                }
            }
            return member;
        } catch (Exception e) {
            throw new RuntimeException("insertMember error", e);
        }
    }

    @Override
    public MemberDTO updateMember(MemberDTO member) {
        String sql = "UPDATE member SET pass=?, name=?, email=?, role=?, status=?, email_verified=?, fail_count=?, last_login=?, updated_at=?, deleted_at=? WHERE id_member=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getPass());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getRole());
            pstmt.setString(5, member.getStatus());
            pstmt.setString(6, member.getEmailVerified());
            pstmt.setInt(7, member.getFailCount() != null ? member.getFailCount() : 0);
            pstmt.setTimestamp(8, member.getLastLogin() != null ? Timestamp.valueOf(member.getLastLogin()) : null);
            pstmt.setTimestamp(9, member.getUpdatedAt() != null ? Timestamp.valueOf(member.getUpdatedAt()) : null);
            pstmt.setTimestamp(10, member.getDeletedAt() != null ? Timestamp.valueOf(member.getDeletedAt()) : null);
            pstmt.setLong(11, member.getIdMember());
            pstmt.executeUpdate();
            return member;
        } catch (Exception e) {
            throw new RuntimeException("updateMember error", e);
        }
    }

    @Override
    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id_member=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("deleteMember error", e);
        }
    }

    private MemberDTO mapRow(ResultSet rs) throws SQLException {
        MemberDTO m = new MemberDTO();
        m.setIdMember(rs.getLong("id_member"));
        m.setId(rs.getString("id"));
        m.setPass(rs.getString("pass"));
        m.setName(rs.getString("name"));
        m.setEmail(rs.getString("email"));
        Timestamp registDay = rs.getTimestamp("regist_day");
        if (registDay != null) m.setRegistDay(registDay.toLocalDateTime());
        m.setRole(rs.getString("role"));
        m.setStatus(rs.getString("status"));
        m.setEmailVerified(rs.getString("email_verified"));
        m.setFailCount(rs.getInt("fail_count"));
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) m.setLastLogin(lastLogin.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) m.setUpdatedAt(updatedAt.toLocalDateTime());
        Timestamp deletedAt = rs.getTimestamp("deleted_at");
        if (deletedAt != null) m.setDeletedAt(deletedAt.toLocalDateTime());
        return m;
    }
}
