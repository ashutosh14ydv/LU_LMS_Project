package com.lms.service;

import com.lms.dao.BookDAO;
import com.lms.dao.MemberDAO;
import com.lms.dao.TransactionDAO;
import com.lms.model.Book;
import com.lms.model.Member;
import com.lms.model.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LibraryService {
    private static LibraryService instance;
    
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final TransactionDAO transactionDAO;

    private LibraryService() {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        transactionDAO = new TransactionDAO();
    }

    public static synchronized LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }

    public boolean issueBook(int bookId, int memberId, LocalDate customDueDate) throws Exception {
        // Business logic: check if user has reached max books (2)
        int count = transactionDAO.getActiveLoanCount(memberId);
        if (count >= 2) {
            throw new Exception("Member has already issued the maximum limit of 2 books.");
        }

        // Check availability
        Book book = bookDAO.getBookById(bookId);
        if (book == null || book.getAvailable() <= 0) {
            throw new Exception("Book is currently not available in stock.");
        }

        // Process Transaction
        LocalDate today = LocalDate.now();
        LocalDate dueDate = customDueDate != null ? customDueDate : today.plusDays(10); 

        Transaction transaction = new Transaction();
        transaction.setBookId(bookId);
        transaction.setMemberId(memberId);
        transaction.setIssueDate(today.toString());
        transaction.setDueDate(dueDate.toString());

        boolean success = transactionDAO.issueBook(transaction);
        if (success) {
            bookDAO.updateAvailable(bookId, -1);
        }
        return success;
    }

    public double returnBook(int transactionId, int bookId, String issueDateStr, LocalDate chosenReturnDate) throws SQLException {
        LocalDate returnDate = chosenReturnDate != null ? chosenReturnDate : LocalDate.now();
        LocalDate issueDate = LocalDate.parse(issueDateStr);
        
        double fine = 0;
        long totalDays = ChronoUnit.DAYS.between(issueDate, returnDate);
        
        if (totalDays > 10) {
            long overdueDays = totalDays - 10;
            
            // Fetch Book to get price
            Book book = bookDAO.getBookById(bookId);
            double bookPrice = book != null ? book.getPrice() : 0.0;
            
            // Formula: 0.25 * Price Per day overdue
            fine = overdueDays * (0.25 * bookPrice);
        }

        boolean success = transactionDAO.returnBook(transactionId, returnDate.toString(), fine);
        if (success) {
            bookDAO.updateAvailable(bookId, 1);
            return fine;
        }
        throw new SQLException("Failed to update transaction record.");
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDAO.getAllBooks();
    }
    
    public boolean addBook(Book b) throws SQLException {
        return bookDAO.addBook(b);
    }

    public List<Member> getAllMembers() throws SQLException {
        return memberDAO.getAllMembers();
    }

    public boolean addMember(Member m) throws SQLException {
        return memberDAO.addMember(m) > 0;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionDAO.getAllTransactions();
    }

    public boolean clearFine(int txId) throws SQLException {
        return transactionDAO.clearFine(txId);
    }
}
