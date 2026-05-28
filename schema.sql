-- MySQL Database Schema for Library Management System
-- Run this in MySQL Workbench or your MySQL Client

-- 1. Create Database
CREATE DATABASE IF NOT EXISTS lms_db;
USE lms_db;

-- 2. Create Members Table
CREATE TABLE IF NOT EXISTS members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    roll_no VARCHAR(50) UNIQUE,
    address VARCHAR(500)
);

-- 3. Create Books Table
CREATE TABLE IF NOT EXISTS books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(50) UNIQUE NOT NULL,
    publisher VARCHAR(100),
    quantity INT DEFAULT 1,
    available INT DEFAULT 1,
    price DECIMAL(10,2) DEFAULT 0.0
);

-- 4. Create Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    member_id INT,
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- 5. Create Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    member_id INT,
    issue_date DATE,
    due_date DATE,
    return_date DATE,
    fine DECIMAL(10,2) DEFAULT 0.0,
    status VARCHAR(20),
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- 6. Insert Default User Data
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'LIBRARIAN');
INSERT IGNORE INTO users (username, password, role) VALUES ('student', 'student123', 'STUDENT');

-- 7. Seed Sample Books Data
INSERT IGNORE INTO books (title, author, isbn, publisher, quantity, available, price) VALUES 
('Software Engineering', 'R. Pressman', 'ISBN-SWE-001', 'McGrawHill', 5, 5, 750.0),
('DBMS Concepts', 'Korth', 'ISBN-DB-002', 'Pearson', 4, 4, 600.0),
('Compiler Design', 'Aho Ullman', 'ISBN-CD-003', 'Addison', 3, 3, 550.0),
('Core Java', 'Cay Horstmann', 'ISBN-JAVA-004', 'Oracle Press', 10, 10, 800.0),
('Python Programming', 'Reema Thareja', 'ISBN-PY-005', 'Oxford', 7, 7, 450.0);
