package com.lms.controller;

import com.lms.model.Book;
import com.lms.model.Member;
import com.lms.service.LibraryService;
import com.lms.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class IssueController implements Initializable {

    @FXML private ComboBox<Member> comboMember;
    @FXML private ComboBox<Book> comboBook;
    @FXML private DatePicker datePickerDue;

    private final LibraryService service = LibraryService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupConverters();
        loadData();
        datePickerDue.setValue(LocalDate.now().plusDays(10));
    }

    private void setupConverters() {
        comboMember.setConverter(new StringConverter<>() {
            @Override public String toString(Member m) { return m == null ? "" : m.getName() + " (" + m.getRollNo() + ")"; }
            @Override public Member fromString(String s) { return null; }
        });
        comboBook.setConverter(new StringConverter<>() {
            @Override public String toString(Book b) { return b == null ? "" : b.getTitle() + " [Avail: " + b.getAvailable() + "]"; }
            @Override public Book fromString(String s) { return null; }
        });
    }

    private void loadData() {
        try {
            comboMember.setItems(FXCollections.observableArrayList(service.getAllMembers()));
            comboBook.setItems(FXCollections.observableArrayList(service.getAllBooks()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleIssue() {
        Member m = comboMember.getValue();
        Book b = comboBook.getValue();

        if (m == null || b == null) {
            AlertUtil.showError("Selection Error", "Please select both a member and a book.");
            return;
        }

        try {
            if (service.issueBook(b.getId(), m.getId(), datePickerDue.getValue())) {
                AlertUtil.showInfo("Success", "Book issued successfully to " + m.getName());
                loadData(); // Refresh available count
            }
        } catch (Exception e) {
            AlertUtil.showError("Action Denied", e.getMessage());
        }
    }
}
