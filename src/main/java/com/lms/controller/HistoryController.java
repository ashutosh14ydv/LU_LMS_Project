package com.lms.controller;

import com.lms.model.Transaction;
import com.lms.service.LibraryService;
import com.lms.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HistoryController implements Initializable {

    @FXML private TableView<Transaction> tblHistory;
    @FXML private TableColumn<Transaction, Integer> colId;
    @FXML private TableColumn<Transaction, String> colMember, colBook, colIssue, colDue, colReturn, colStatus;
    @FXML private TableColumn<Transaction, Double> colFine;

    private final LibraryService service = LibraryService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMember.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBook.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colIssue.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colDue.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colReturn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            List<Transaction> all = service.getAllTransactions();
            
            // Role-based filtering
            var user = SessionManager.getCurrentUser();
            if (user != null && "STUDENT".equals(user.getRole())) {
                 int studentId = user.getMemberId();
                 all = all.stream()
                         .filter(t -> t.getMemberId() == studentId)
                         .collect(Collectors.toList());
            }
            
            tblHistory.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
