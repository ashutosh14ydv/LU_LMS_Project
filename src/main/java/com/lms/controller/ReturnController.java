package com.lms.controller;

import com.lms.model.Transaction;
import com.lms.service.LibraryService;
import com.lms.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReturnController implements Initializable {

    @FXML private TableView<Transaction> tblIssued;
    @FXML private TableColumn<Transaction, Integer> colId;
    @FXML private TableColumn<Transaction, String> colMember, colBook, colIssueDate, colDueDate;
    @FXML private DatePicker datePickerReturn;

    private final LibraryService service = LibraryService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePickerReturn.setValue(LocalDate.now());
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMember.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBook.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        loadData();
    }

    private void loadData() {
        try {
            List<Transaction> issuedOnly = service.getAllTransactions().stream()
                    .filter(t -> "ISSUED".equals(t.getStatus()))
                    .collect(Collectors.toList());
            tblIssued.setItems(FXCollections.observableArrayList(issuedOnly));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturn() {
        Transaction selected = tblIssued.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showError("No Selection", "Please select a transaction from the table.");
            return;
        }

        try {
            double fineCalculated = service.returnBook(selected.getId(), selected.getBookId(), selected.getIssueDate(), datePickerReturn.getValue());
            String msg = "Book returned successfully!";
            if (fineCalculated > 0) {
                msg += "\nFine imposed: ₹ " + String.format("%.2f", fineCalculated);
                msg += "\n(Reason: Exceeded 10 days limit @ 25% Book Price/day)";
            } else {
                msg += "\nNo fine applied (Returned within grace period).";
            }
            AlertUtil.showInfo("Process Result", msg);
            loadData();
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }
}
