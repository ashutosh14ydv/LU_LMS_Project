package com.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lms_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "@A546719"; // Updated with confirmed password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void initializeDatabase() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(50) NOT NULL, " +
                "role VARCHAR(20) NOT NULL, " +
                "member_id INT, " +
                "FOREIGN KEY (member_id) REFERENCES members(id)" +
                ");";

        String booksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "title VARCHAR(255) NOT NULL, " +
                "author VARCHAR(255) NOT NULL, " +
                "isbn VARCHAR(50) UNIQUE NOT NULL, " +
                "publisher VARCHAR(100), " +
                "quantity INT DEFAULT 1, " +
                "available INT DEFAULT 1, " +
                "price DECIMAL(10,2) DEFAULT 0.0" +
                ");";

        String membersTable = "CREATE TABLE IF NOT EXISTS members (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "phone VARCHAR(15), " +
                "roll_no VARCHAR(50) UNIQUE, " +
                "address VARCHAR(500)" +
                ");";

        String transactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "book_id INT, " +
                "member_id INT, " +
                "issue_date DATE, " +
                "due_date DATE, " +
                "return_date DATE, " +
                "fine DECIMAL(10,2) DEFAULT 0.0, " +
                "status VARCHAR(20), " +
                "FOREIGN KEY (book_id) REFERENCES books(id), " +
                "FOREIGN KEY (member_id) REFERENCES members(id)" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Order matters due to foreign keys. Create members first.
            stmt.execute(membersTable);
            stmt.execute(booksTable);
            stmt.execute(usersTable);
            stmt.execute(transactionsTable);
            
            // Insert default librarian if not exists
            String defaultAdmin = "INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'LIBRARIAN');";
            String defaultStudent = "INSERT IGNORE INTO users (username, password, role) VALUES ('student', 'student123', 'STUDENT');";
            stmt.execute(defaultAdmin);
            stmt.execute(defaultStudent);

            // Seed Books if empty
            String checkBooks = "SELECT count(*) FROM books";
            try (ResultSet rs = stmt.executeQuery(checkBooks)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO books (title, author, isbn, publisher, quantity, available, price) VALUES ('Software Engineering', 'R. Pressman', 'ISBN-SWE-001', 'McGrawHill', 5, 5, 750.0);");
                    stmt.execute("INSERT INTO books (title, author, isbn, publisher, quantity, available, price) VALUES ('DBMS Concepts', 'Korth', 'ISBN-DB-002', 'Pearson', 4, 4, 600.0);");
                    stmt.execute("INSERT INTO books (title, author, isbn, publisher, quantity, available, price) VALUES ('Compiler Design', 'Aho Ullman', 'ISBN-CD-003', 'Addison', 3, 3, 550.0);");
                    stmt.execute("INSERT INTO books (title, author, isbn, publisher, quantity, available, price) VALUES ('Core Java', 'Cay Horstmann', 'ISBN-JAVA-004', 'Oracle Press', 10, 10, 800.0);");
                    stmt.execute("INSERT INTO books (title, author, isbn, publisher, quantity, available, price) VALUES ('Python Programming', 'Reema Thareja', 'ISBN-PY-005', 'Oxford', 7, 7, 450.0);");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database initialization failed! Check MySQL service status and credentials.");
            e.printStackTrace();
        }
    }
}
