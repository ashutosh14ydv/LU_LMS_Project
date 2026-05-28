package com.lms.dao;

import com.lms.model.Transaction;
import com.lms.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean issueBook(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (book_id, member_id, issue_date, due_date, fine, status) VALUES (?, ?, ?, ?, 0, 'ISSUED')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, t.getBookId());
            pstmt.setInt(2, t.getMemberId());
            pstmt.setString(3, t.getIssueDate());
            pstmt.setString(4, t.getDueDate());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean returnBook(int transactionId, String returnDate, double fine) throws SQLException {
        String sql = "UPDATE transactions SET return_date = ?, fine = ?, status = 'RETURNED' WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, returnDate);
            pstmt.setDouble(2, fine);
            pstmt.setInt(3, transactionId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT t.*, b.title, m.name FROM transactions t " +
                     "JOIN books b ON t.book_id = b.id " +
                     "JOIN members m ON t.member_id = m.id";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("issue_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date"),
                        rs.getDouble("fine"),
                        rs.getString("status")
                );
                t.setBookTitle(rs.getString("title"));
                t.setMemberName(rs.getString("name"));
                list.add(t);
            }
        }
        return list;
    }

    public int getActiveLoanCount(int memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM transactions WHERE member_id = ? AND status = 'ISSUED'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    public boolean clearFine(int txId) throws SQLException {
        String sql = "UPDATE transactions SET fine = 0 WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, txId);
            return pstmt.executeUpdate() > 0;
        }
    }
}
