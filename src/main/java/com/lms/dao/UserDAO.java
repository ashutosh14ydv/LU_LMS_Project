package com.lms.dao;

import com.lms.model.User;
import com.lms.util.DatabaseUtil;
import java.sql.*;

public class UserDAO {

    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getInt("member_id")
                    );
                }
            }
        }
        return null;
    }

    public boolean register(String username, String password, String role, Integer memberId) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, member_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            if (memberId != null && memberId != 0) {
                pstmt.setInt(4, memberId);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            return pstmt.executeUpdate() > 0;
        }
    }
}
