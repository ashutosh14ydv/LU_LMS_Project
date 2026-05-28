package com.lms.dao;

import com.lms.model.Book;
import com.lms.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public boolean addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn, publisher, quantity, available, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setInt(6, book.getQuantity()); // available initially same as total
            pstmt.setDouble(7, book.getPrice());
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("publisher"),
                        rs.getInt("quantity"),
                        rs.getInt("available"),
                        rs.getDouble("price")
                ));
            }
        }
        return books;
    }

    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("publisher"),
                        rs.getInt("quantity"),
                        rs.getInt("available"),
                        rs.getDouble("price")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, publisher=?, quantity=?, available=?, price=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setInt(6, book.getAvailable());
            pstmt.setDouble(7, book.getPrice());
            pstmt.setInt(8, book.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public void updateAvailable(int bookId, int diff) throws SQLException {
        String sql = "UPDATE books SET available = available + ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, diff);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        }
    }
}
