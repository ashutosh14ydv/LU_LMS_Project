package com.lms.dao;

import com.lms.model.Member;
import com.lms.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public int addMember(Member m) throws SQLException {
        String sql = "INSERT INTO members (name, email, phone, roll_no, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, m.getName());
            pstmt.setString(2, m.getEmail());
            pstmt.setString(3, m.getPhone());
            pstmt.setString(4, m.getRollNo());
            pstmt.setString(5, m.getAddress());
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("roll_no"),
                        rs.getString("address")
                ));
            }
        }
        return list;
    }

    public Member getMemberById(int id) throws SQLException {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("roll_no"),
                        rs.getString("address")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateMember(Member m) throws SQLException {
        String sql = "UPDATE members SET name=?, email=?, phone=?, roll_no=?, address=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, m.getName());
            pstmt.setString(2, m.getEmail());
            pstmt.setString(3, m.getPhone());
            pstmt.setString(4, m.getRollNo());
            pstmt.setString(5, m.getAddress());
            pstmt.setInt(6, m.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteMember(int id) throws SQLException {
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    public Member getMemberByRollNo(String rollNo) throws SQLException {
        String sql = "SELECT * FROM members WHERE roll_no = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("roll_no"),
                        rs.getString("address")
                    );
                }
            }
        }
        return null;
    }
}
