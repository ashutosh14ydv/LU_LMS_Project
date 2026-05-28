package com.lms.controller;

import com.lms.service.LibraryService;
import com.lms.model.Transaction;
import com.lms.util.SessionManager;
import com.lms.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController implements Initializable {

    @FXML private Label lblTotalBooks, lblTotalMembers, lblTotalFines;
    @FXML private VBox paneFineActions;
    @FXML private TableView<Transaction> tblDues;
    @FXML private TableColumn<Transaction, Integer> colTxId;
    @FXML private TableColumn<Transaction, String> colMemberName, colBookTitle;
    @FXML private TableColumn<Transaction, Double> colFineAmount;
    @FXML private Button btnClearFine;

    private final LibraryService service = LibraryService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTxId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colFineAmount.setCellValueFactory(new PropertyValueFactory<>("fine"));

        refreshData();
    }

    private void refreshData() {
        try {
            var user = SessionManager.getCurrentUser();
            List<Transaction> allTxs = service.getAllTransactions();
            
            // Lock clear button if not Librarian
            boolean isLibrarian = (user != null && "LIBRARIAN".equals(user.getRole()));
            btnClearFine.setDisable(!isLibrarian);
            if (!isLibrarian) {
                btnClearFine.setText("Visit Librarian to Pay");
            }

            List<Transaction> filteredTxs;
            
            if (user != null && "STUDENT".equals(user.getRole())) {
                int sid = user.getMemberId();
                filteredTxs = allTxs.stream().filter(t -> t.getMemberId() == sid).collect(Collectors.toList());
                
                long active = filteredTxs.stream().filter(t -> "ISSUED".equals(t.getStatus())).count();
                double myFine = filteredTxs.stream().mapToDouble(Transaction::getFine).sum();
                
                lblTotalBooks.setText(active + " (Active)");
                lblTotalMembers.setText("Personal Account");
                lblTotalFines.setText("₹ " + String.format("%.2f", myFine));
            } else {
                filteredTxs = allTxs; // admin sees everything
                lblTotalBooks.setText(String.valueOf(service.getAllBooks().size()));
                lblTotalMembers.setText(String.valueOf(service.getAllMembers().size()));
                double totalFine = allTxs.stream().mapToDouble(Transaction::getFine).sum();
                lblTotalFines.setText("₹ " + String.format("%.2f", totalFine));
            }

            // Populate bottom table with only active unpaid fines (> 0)
            List<Transaction> overdueOnly = filteredTxs.stream()
                    .filter(t -> t.getFine() > 0)
                    .collect(Collectors.toList());
            
            tblDues.setItems(FXCollections.observableArrayList(overdueOnly));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClearPayment() {
        Transaction selected = tblDues.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showError("Selection", "Please select a row from the table first.");
            return;
        }

        try {
            if (service.clearFine(selected.getId())) {
                AlertUtil.showInfo("Payment Successful", "The transaction fine has been cleared to 0.");
                refreshData(); // reload numbers
            }
        } catch (Exception e) {
            AlertUtil.showError("DB Error", e.getMessage());
        }
    }
}
